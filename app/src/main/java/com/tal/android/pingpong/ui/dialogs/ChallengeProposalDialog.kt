package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Guideline
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueue
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import com.tal.android.pingpong.utils.asPercentString
import kotlin.math.max
import kotlin.math.min

class ChallengeProposalDialog(private val match: MatchRecord, private val bus: Bus) {

    private var dialog: AlertDialog? = null

    fun show(activity: Context) {
        val challengeDialogView = LayoutInflater
            .from(activity)
            .inflate(R.layout.challenge_proposal_dialog, null)
        val localUserImage = challengeDialogView.findViewById<ImageView>(R.id.local_image)
        val localUserName = challengeDialogView.findViewById<TextView>(R.id.user_name)
        val localUserEmail = challengeDialogView.findViewById<TextView>(R.id.user_email)
        val localUserMatchesWon = challengeDialogView.findViewById<TextView>(R.id.user_matches_won)
        val localUserMatchesLost = challengeDialogView.findViewById<TextView>(R.id.user_matches_lost)
        val localUserWinRate = challengeDialogView.findViewById<TextView>(R.id.user_matches_win_rate)
        val winChancesLabel = challengeDialogView.findViewById<TextView>(R.id.win_chances_label)
        val difficultyLevelGuideline = challengeDialogView.findViewById<Guideline>(R.id.guideline)

        val matchDate = challengeDialogView.findViewById<TextView>(R.id.match_date)
        val visitorUserImage = challengeDialogView.findViewById<ImageView>(R.id.visitor_image)

        val localUser = match.local ?: return
        val visitorUser = match.visitor ?: return

        GlideUtils.loadImage(localUser.userImage, localUserImage, R.drawable.ic_incognito, true)
        GlideUtils.loadImage(visitorUser.userImage, visitorUserImage, R.drawable.ic_incognito, true)

        localUserName.text = activity.getString(R.string.x_stats, localUser.userName)
        localUserEmail.text = localUser.userEmail
        matchDate.text = DateUtils.formatDate(match.matchDate)

        localUserMatchesWon.text = activity.getString(
            R.string.x_matches_won,
            localUser.firstName(),
            localUser.matchesWon
        )

        localUserMatchesLost.text = activity.getString(
            R.string.x_matches_lost,
            localUser.firstName(),
            localUser.matchesLost
        )

        localUserWinRate.text = activity.getString(
            R.string.x_matches_win_rate,
            localUser.firstName(),
            localUser.matchesRatio
        )

        val winChances = visitorUser.chancesToWin(localUser)
        if (winChances == User.UNKNOWN) {
            difficultyLevelGuideline.setGuidelinePercent(User.CHANCES_HALF)
            winChancesLabel.text = activity.getString(R.string.unknown)
        } else {
            difficultyLevelGuideline.setGuidelinePercent(winChances)
            winChancesLabel.text = (100 * winChances).asPercentString(digits = 0)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.incoming_challenge)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.accept)
            .setNegativeButtonText(R.string.decline)
            .setNeutralButtonText(R.string.cancel)
            .setPositiveButtonListener {
                bus.post(AcceptChallengeButtonClickedEvent())
            }
            .setNegativeButtonListener {
                bus.post(DeclineChallengeButtonClickedEvent())
            }
            .build(activity)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    class AcceptChallengeButtonClickedEvent
    class DeclineChallengeButtonClickedEvent
}
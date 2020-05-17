package com.tal.android.pingpong.ui.dialogs

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.UsersStatsAdapter
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load

class IncomingDoublesMatchDialog(private val match: MatchRecord, private val bus: Bus) {

    private var dialog: AlertDialog? = null

    fun show(fragmentActivity: FragmentActivity) {
        val challengeDialogView = LayoutInflater
            .from(fragmentActivity)
            .inflate(R.layout.doubles_incoming_challenge_dialog, null)
        val localUserImage: ImageView = challengeDialogView.findViewById(R.id.local_image)
        val localCompanionUserImage: ImageView = challengeDialogView.findViewById(R.id.local_companion_image)
        val visitorUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_image)
        val visitorCompanionUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_companion_image)

        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        val matchDate: TextView = challengeDialogView.findViewById(R.id.match_date)
        val usersStats: ViewPager2 = challengeDialogView.findViewById(R.id.users_stats)

        val localUser = match.local ?: return
        val localCompanionUser = match.localCompanion ?: return
        val visitorUser = match.visitor ?: return
        val visitorCompanionUser = match.visitorCompanion ?: return

        localUserImage.load(localUser.userImage, R.drawable.ic_incognito, true)
        localCompanionUserImage.load(localCompanionUser.userImage, R.drawable.ic_incognito, true)
        visitorUserImage.load(visitorUser.userImage, R.drawable.ic_incognito, true)
        visitorCompanionUserImage.load(visitorCompanionUser.userImage, R.drawable.ic_incognito, true)

        difficultyBar.setup(localUser.matchesRatioValue, visitorUser.matchesRatioValue)

        usersStats.adapter = UsersStatsAdapter(fragmentActivity, localUser, localCompanionUser, visitorUser, visitorCompanionUser)
        usersStats.offscreenPageLimit = usersStats.adapter?.itemCount ?: 2

        matchDate.text = DateUtils.formatDate(match.matchDate)

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
                bus.post(AcceptChallengeButtonClickedEvent(match))
            }
            .setNegativeButtonListener {
                DeclineMatchDialog(match, bus).show(fragmentActivity)
            }
            .build(fragmentActivity)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    class AcceptChallengeButtonClickedEvent(val match: MatchRecord)
    class DeclineChallengeButtonClickedEvent(val match: MatchRecord)
}
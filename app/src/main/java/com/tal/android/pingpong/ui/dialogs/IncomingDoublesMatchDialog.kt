package com.tal.android.pingpong.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.recyclerview.UsersStatsAdapter
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.*

class IncomingDoublesMatchDialog(private val match: MatchRecord, private val myUserId: Int?, private val bus: Bus) : BaseDialog() {

    fun show(fragmentActivity: FragmentActivity) {
        val challengeDialogView = LayoutInflater
            .from(fragmentActivity)
            .inflate(R.layout.doubles_incoming_challenge_dialog, null)

        val localUserImage: ImageView = challengeDialogView.findViewById(R.id.local_user_image)
        val localCompanionUserImage: ImageView = challengeDialogView.findViewById(R.id.local_companion_image)
        val visitorUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_image)
        val visitorCompanionUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_companion_image)

        val localUserConfirmedImage: ImageView = challengeDialogView.findViewById(R.id.local_user_confirmed)
        val localCompanionUserConfirmedImage: ImageView = challengeDialogView.findViewById(R.id.local_companion_user_confirmed)
        val visitorUserConfirmedImage: ImageView = challengeDialogView.findViewById(R.id.visitor_user_confirmed)
        val visitorCompanionUserConfirmedImage: ImageView = challengeDialogView.findViewById(R.id.visitor_companion_user_confirmed)

        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        val matchDate: TextView = challengeDialogView.findViewById(R.id.match_date)
        val usersStats: ViewPager2 = challengeDialogView.findViewById(R.id.users_stats)

        val localUser = match.local ?: return
        val localCompanionUser = match.localCompanion ?: return
        val visitorUser = match.visitor ?: return
        val visitorCompanionUser = match.visitorCompanion ?: return

        localUserImage.load(
            url = localUser.userImage,
            fallbackImage = R.drawable.ic_incognito,
            rounded = true,
            alpha = getUserImageAlpha(true)
        )
        localCompanionUserImage.load(
            url = localCompanionUser.userImage,
            fallbackImage = R.drawable.ic_incognito,
            rounded = true,
            alpha = getUserImageAlpha(match.localCompanionUserConfirmed)
        )
        visitorUserImage.load(
            url = visitorUser.userImage,
            fallbackImage = R.drawable.ic_incognito,
            rounded = true,
            alpha = getUserImageAlpha(match.visitorUserConfirmed)
        )
        visitorCompanionUserImage.load(
            url = visitorCompanionUser.userImage,
            fallbackImage = R.drawable.ic_incognito,
            rounded = true,
            alpha = getUserImageAlpha(match.visitorCompanionUserConfirmed)
        )

        localUserConfirmedImage.visibility = View.VISIBLE
        localCompanionUserConfirmedImage.visibility = getConfirmedVisibility(match.localCompanionUserConfirmed)
        visitorUserConfirmedImage.visibility = getConfirmedVisibility(match.visitorUserConfirmed)
        visitorCompanionUserConfirmedImage.visibility = getConfirmedVisibility(match.visitorCompanionUserConfirmed)

        difficultyBar.setup(localUser.matchesRatioValue, visitorUser.matchesRatioValue)

        usersStats.apply {
            adapter = UsersStatsAdapter(
                fragmentActivity,
                match,
                localUser,
                localCompanionUser,
                visitorUser,
                visitorCompanionUser
            )
            offscreenPageLimit = usersStats.adapter?.itemCount ?: 2
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        matchDate.text = DateUtils.formatDate(match.matchDate)

        val builder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setTitle(R.string.incoming_challenge)
            .setView(challengeDialogView)
        if (match.userConfirmedDoublesMatch(myUserId)) {
            builder
                .setAutoDismiss(true)
                .setPositiveButtonText(R.string.close)
        } else {
            builder
                .setAutoDismiss(false)
                .setPositiveButtonText(R.string.accept)
                .setNegativeButtonText(R.string.decline)
                .setNeutralButtonText(R.string.cancel)
                .setPositiveButtonListener {
                    bus.post(AcceptChallengeButtonClickedEvent(match))
                }
                .setNegativeButtonListener {
                    DeclineMatchDialog(match, bus).show(fragmentActivity)
                }
        }
        dialog = builder.build(fragmentActivity)
        dialog?.show()
    }

    class AcceptChallengeButtonClickedEvent(val match: MatchRecord)
}
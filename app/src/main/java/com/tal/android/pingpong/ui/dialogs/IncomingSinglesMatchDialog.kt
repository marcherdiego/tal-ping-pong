package com.tal.android.pingpong.ui.dialogs

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.UsersStatsAdapter
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load

class IncomingSinglesMatchDialog(private val match: MatchRecord, private val myUserId: Int?, private val bus: Bus) : BaseDialog() {

    fun show(fragmentActivity: FragmentActivity) {
        val challengeDialogView = LayoutInflater
            .from(fragmentActivity)
            .inflate(R.layout.singles_incoming_challenge_dialog, null)
        val localUserImage: ImageView = challengeDialogView.findViewById(R.id.local_user_image)

        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)

        val matchDate: TextView = challengeDialogView.findViewById(R.id.match_date)
        val visitorUserImage: ImageView = challengeDialogView.findViewById(R.id.visitor_image)
        val usersStats: ViewPager2 = challengeDialogView.findViewById(R.id.users_stats)

        val localUser = match.local ?: return
        val visitorUser = match.visitor ?: return

        val (myUser, rivalUser) = if (match.isLocalUser(myUserId)) {
            Pair(localUser, visitorUser)
        } else {
            Pair(visitorUser, localUser)
        }

        localUserImage.load(localUser.userImage, R.drawable.ic_incognito, true)
        visitorUserImage.load(visitorUser.userImage, R.drawable.ic_incognito, true)

        difficultyBar.setup(myUser.matchesRatioValue, rivalUser.matchesRatioValue)

        usersStats.apply {
            adapter = UsersStatsAdapter(fragmentActivity, match, localUser, visitorUser)
            offscreenPageLimit = usersStats.adapter?.itemCount ?: 2
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        matchDate.text = DateUtils.formatDate(match.matchDate)

        val dialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
        if (match.isLocalUser(myUserId)) {
            dialogBuilder
                .setAutoDismiss(true)
                .setTitle(R.string.challenge)
                .setView(challengeDialogView)
                .setPositiveButtonText(R.string.close)
        } else {
            dialogBuilder
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
        }
        dialog = dialogBuilder.build(fragmentActivity)
        dialog?.show()
    }

    class AcceptChallengeButtonClickedEvent(val match: MatchRecord)
}
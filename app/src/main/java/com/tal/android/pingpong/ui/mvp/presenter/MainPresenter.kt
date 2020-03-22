package com.tal.android.pingpong.ui.mvp.presenter

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.fragments.*

import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.view.MainView
import com.tal.android.pingpong.ui.mvp.view.MatchesListView
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import com.tal.android.pingpong.utils.asPercentString
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

    init {
        onNavigationItemSelected(MainView.NavigationItemSelectedEvent(R.id.menu_matches))
        model.challengeMatch?.let { match ->
            openMatchChallengeDialog(match)
        }
    }

    private fun openMatchChallengeDialog(match: MatchRecord) {
        view.activity?.let { activity ->
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
            difficultyLevelGuideline.setGuidelinePercent(winChances)
            winChancesLabel.text = (100 * winChances).asPercentString(digits = 0)

            DialogFactory
                .Builder()
                .setCancelable(true)
                .setTitle(R.string.challenge)
                .setView(challengeDialogView)
                .setPositiveButtonText(R.string.accept)
                .setPositiveButtonListener {
                    model.acceptChallenge(match)
                }
                .setNegativeButtonText(R.string.cancel)
                .build(activity)
                .show()
        }
    }

    @Subscribe
    fun onNavigationItemSelected(event: MainView.NavigationItemSelectedEvent) {
        val currentState = model.currentState
        val (newState, fragment) = when (event.itemId) {
            R.id.menu_matches -> Pair(MainModel.MATCHES, MatchesListFragment())
            R.id.menu_new_match -> Pair(MainModel.FIND_RIVAL, UsersListFragment())
            R.id.menu_ranking -> Pair(MainModel.RANKING, RankingFragment())
            R.id.menu_profile -> Pair(MainModel.PROFILE, ProfileFragment())
            else -> return
        }
        model.currentState = newState
        if (currentState == model.currentState) {
            //If re-selecting tab, ignore event
            return
        }
        updateCurrentFragment(fragment)
    }

    @Subscribe
    fun onNewMatchButtonClicked(event: MatchesListView.NewMatchButtonClickedEvent) {
        view.setSelectedTab(R.id.menu_new_match)
    }

    private fun updateCurrentFragment(fragment: Fragment) {
        view.withFragmentTransaction {
            if (!fragment.isAdded) {
                replace(R.id.frame_container, fragment)
            }
            commitNow()
        }
    }

    override fun onBackPressed(): Boolean {
        return if (model.currentState == MainModel.MATCHES) {
            super.onBackPressed()
        } else {
            view.setSelectedTab(R.id.menu_matches)
            true
        }
    }
}

package com.tal.android.pingpong.ui.mvp.presenter

import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.dialogs.ChallengeProposalDialog
import com.tal.android.pingpong.ui.fragments.*

import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.view.MainView
import com.tal.android.pingpong.ui.mvp.view.MatchesListView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var challengeProposalDialog: ChallengeProposalDialog? = null

    init {
        onNavigationItemSelected(MainView.NavigationItemSelectedEvent(R.id.menu_matches))
        model.challengeMatch?.let { match ->
            openMatchChallengeDialog(match)
        }
    }

    private fun openMatchChallengeDialog(match: MatchRecord) {
        view.activity?.let {
            challengeProposalDialog = ChallengeProposalDialog(match, model.getBus())
            challengeProposalDialog?.show(it)
        }
    }

    @Subscribe
    fun onNavigationItemSelected(event: MainView.NavigationItemSelectedEvent) {
        val currentState = model.currentState
        val (newState, fragment) = when (event.itemId) {
            R.id.menu_matches -> Pair(MainModel.MATCHES, MatchesListFragment())
            R.id.menu_new_match -> Pair(MainModel.FIND_RIVAL, UsersListFragment())
            R.id.menu_ranking -> Pair(MainModel.RANKING, RankingFragment())
            R.id.menu_profile -> Pair(MainModel.PROFILE, UserProfileFragment())
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

    /***
     * ACCEPT / DECLINE CHALLENGE EVENTS
     */

    @Subscribe
    fun onAcceptChallengeButtonClicked(event: ChallengeProposalDialog.AcceptChallengeButtonClickedEvent) {
        model.acceptChallenge()
    }

    @Subscribe
    fun onDeclineChallengeButtonClicked(event: ChallengeProposalDialog.DeclineChallengeButtonClickedEvent) {
        model.declineChallenge()
    }

    @Subscribe
    fun onChallengeAcceptedSuccessfully(event: MainModel.ChallengeAcceptedSuccessfullyEvent) {
        view.showToast(R.string.challenge_accepted)
        challengeProposalDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeAcceptFailed(event: MainModel.ChallengeAcceptFailedEvent) {
        view.showToast(R.string.failed_to_accept_challenge)
    }

    @Subscribe
    fun onChallengeDeclinedSuccessfully(event: MainModel.ChallengeDeclinedSuccessfullyEvent) {
        view.showToast(R.string.challenge_declined)
        challengeProposalDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeDeclineFailed(event: MainModel.ChallengeDeclineFailedEvent) {
        view.showToast(R.string.failed_to_decline_challenge)
    }

    /***
     * ACCEPT / DECLINE CHALLENGE EVENTS
     */

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

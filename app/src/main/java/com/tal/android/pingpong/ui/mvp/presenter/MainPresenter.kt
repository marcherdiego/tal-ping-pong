package com.tal.android.pingpong.ui.mvp.presenter

import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.UnconfirmedMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.SinglesChallengeProposalDialog
import com.tal.android.pingpong.ui.fragments.*

import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.view.MainView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var challengeProposalDialog: SinglesChallengeProposalDialog? = null

    init {
        onNavigationItemSelected(MainView.NavigationItemSelectedEvent(R.id.menu_matches))
        model.challengeMatch?.let { match ->
            openMatchChallengeDialog(match)
        }
    }

    private fun openMatchChallengeDialog(match: MatchRecord) {
        view.activity?.let {
            challengeProposalDialog = SinglesChallengeProposalDialog(match, model.getBus())
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
        view.setToolbarTitle(TOOLBAR_TITLE)
        if (newState == MainModel.PROFILE) {
            view.expandToolbar()
        } else {
            view.collapseToolbar()
        }
        updateCurrentFragment(fragment)
    }

    /***
     * ACCEPT / DECLINE CHALLENGE EVENTS
     */

    @Subscribe
    fun onAcceptChallengeButtonClicked(event: SinglesChallengeProposalDialog.AcceptChallengeButtonClickedEvent) {
        model.acceptChallenge(event.match)
    }

    @Subscribe
    fun onDeclineChallengeButtonClicked(event: SinglesChallengeProposalDialog.DeclineChallengeButtonClickedEvent) {
        model.declineChallenge(event.match)
    }

    @Subscribe
    fun onChallengeAcceptedSuccessfully(event: MainModel.ChallengeAcceptedSuccessfullyEvent) {
        view.showToast(R.string.challenge_accepted)
        model.notifyUpdateLists()
        challengeProposalDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeAcceptFailed(event: MainModel.ChallengeAcceptFailedEvent) {
        view.showToast(R.string.failed_to_accept_challenge)
    }

    @Subscribe
    fun onChallengeDeclinedSuccessfully(event: MainModel.ChallengeDeclinedSuccessfullyEvent) {
        view.showToast(R.string.challenge_declined)
        model.notifyUpdateLists()
        challengeProposalDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeDeclineFailed(event: MainModel.ChallengeDeclineFailedEvent) {
        view.showToast(R.string.failed_to_decline_challenge)
    }

    /***
     * ACCEPT / DECLINE CHALLENGE EVENTS
     */

    @Subscribe
    fun onUpcomingMatchClicked(event: UnconfirmedMatchesAdapter.UpcomingMatchClickedEvent) {
        openMatchChallengeDialog(event.matchRecord)
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

    companion object {
        private const val TOOLBAR_TITLE = "TAL Ping Pong"
    }
}

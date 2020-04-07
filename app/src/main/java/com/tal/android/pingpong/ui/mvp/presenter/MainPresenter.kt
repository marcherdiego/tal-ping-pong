package com.tal.android.pingpong.ui.mvp.presenter

import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.UnconfirmedMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.DoublesMatchProposalDialog
import com.tal.android.pingpong.ui.dialogs.SinglesMatchProposalDialog
import com.tal.android.pingpong.ui.fragments.*

import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.view.MainView
import com.tal.android.pingpong.ui.mvp.view.UsersListView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var singlesMatchProposalDialog: SinglesMatchProposalDialog? = null
    private var doublesMatchProposalDialog: DoublesMatchProposalDialog? = null

    init {
        onNavigationItemSelected(MainView.NavigationItemSelectedEvent(R.id.menu_matches))
        model.challengeMatch?.let { match ->
            openSinglesMatchDialog(match)
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
    fun onAcceptChallengeButtonClicked(event: SinglesMatchProposalDialog.AcceptChallengeButtonClickedEvent) {
        model.acceptChallenge(event.match)
    }

    @Subscribe
    fun onDeclineChallengeButtonClicked(event: SinglesMatchProposalDialog.DeclineChallengeButtonClickedEvent) {
        model.declineChallenge(event.match)
    }

    @Subscribe
    fun onChallengeAcceptedSuccessfully(event: MainModel.ChallengeAcceptedSuccessfullyEvent) {
        view.showToast(R.string.challenge_accepted)
        model.notifyUpdateLists()
        singlesMatchProposalDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeAcceptFailed(event: MainModel.ChallengeAcceptFailedEvent) {
        view.showToast(R.string.failed_to_accept_challenge)
    }

    @Subscribe
    fun onChallengeDeclinedSuccessfully(event: MainModel.ChallengeDeclinedSuccessfullyEvent) {
        view.showToast(R.string.challenge_declined)
        model.notifyUpdateLists()
        singlesMatchProposalDialog?.dismiss()
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
        openSinglesMatchDialog(event.matchRecord)
    }

    @Subscribe
    fun onNewDoublesMatchButtonClicked(event: UsersListView.NewDoublesMatchButtonClickedEvent) {
        //TODO handle incoming doubles challenge
    }

    private fun updateCurrentFragment(fragment: Fragment) {
        view.withFragmentTransaction {
            if (!fragment.isAdded) {
                replace(R.id.frame_container, fragment)
            }
            commitNow()
        }
    }

    private fun openSinglesMatchDialog(match: MatchRecord) {
        view.activity?.let {
            singlesMatchProposalDialog = SinglesMatchProposalDialog(match, model.getBus())
            singlesMatchProposalDialog?.show(it)
        }
    }

    private fun openDoublesMatchDialog(match: MatchRecord) {
        view.activity?.let {
            doublesMatchProposalDialog = DoublesMatchProposalDialog(match, model.getBus())
            doublesMatchProposalDialog?.show(it)
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

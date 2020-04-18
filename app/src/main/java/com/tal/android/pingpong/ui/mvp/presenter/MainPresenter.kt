package com.tal.android.pingpong.ui.mvp.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.events.ChallengeSubmitFailedEvent
import com.tal.android.pingpong.events.ChallengeSubmittedSuccessfullyEvent
import com.tal.android.pingpong.notifications.Constants
import com.tal.android.pingpong.ui.adapters.UnconfirmedMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.DeclineMatchDialog
import com.tal.android.pingpong.ui.dialogs.IncomingDoublesMatchDialog
import com.tal.android.pingpong.ui.dialogs.IncomingSinglesMatchDialog
import com.tal.android.pingpong.ui.fragments.*

import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.view.MainView
import org.greenrobot.eventbus.Subscribe
import java.lang.IllegalArgumentException

class MainPresenter(view: MainView, model: MainModel) : BaseActivityPresenter<MainView, MainModel>(view, model) {

    private var incomingSinglesMatchDialog: IncomingSinglesMatchDialog? = null
    private var incomingDoublesMatchDialog: IncomingDoublesMatchDialog? = null

    init {
        val itemId = when (model.initialScreenState) {
            MainModel.UNSET, MainModel.MATCHES -> R.id.menu_matches
            MainModel.FIND_RIVAL -> R.id.menu_new_match
            MainModel.RANKING -> R.id.menu_ranking
            MainModel.PROFILE -> R.id.menu_profile
            else -> throw IllegalArgumentException()
        }
        onNavigationItemSelected(MainView.NavigationItemSelectedEvent(itemId, true))
        when (model.actionType) {
            Constants.INCOMING_SINGLES_CHALLENGE -> {
                model.match?.let { match ->
                    openIncomingSinglesMatchDialog(match)
                }
            }
        }
    }

    @Subscribe
    fun onNavigationItemSelected(event: MainView.NavigationItemSelectedEvent) {
        val currentState = model.currentState
        val (newState, fragment) = when (event.itemId) {
            R.id.menu_matches -> {
                val matchesFragment = MatchesListFragment().apply {
                    if (event.manualEvent) {
                        arguments = Bundle().apply {
                            putSerializable(MatchesListFragment.EXTRA_MATCH, model.match)
                            putString(MatchesListFragment.EXTRA_TAB, model.matchesTabsState)
                            putString(MatchesListFragment.ACTION_TYPE, model.actionType)
                        }
                    }
                }
                Pair(MainModel.MATCHES, matchesFragment)
            }
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
    fun onAcceptChallengeButtonClicked(event: IncomingSinglesMatchDialog.AcceptChallengeButtonClickedEvent) {
        model.acceptChallenge(event.match)
    }

    @Subscribe
    fun onDeclineChallengeButtonClicked(event: DeclineMatchDialog.ConfirmDeclineMatchButtonClickedEvent) {
        model.declineChallenge(event.match, event.declineReason)
    }

    @Subscribe
    fun onChallengeAcceptedSuccessfully(event: MainModel.ChallengeAcceptedSuccessfullyEvent) {
        view.showToast(R.string.challenge_accepted)
        model.notifyUpdateLists()
        incomingSinglesMatchDialog?.dismiss()
    }

    @Subscribe
    fun onChallengeAcceptFailed(event: MainModel.ChallengeAcceptFailedEvent) {
        view.showToast(R.string.failed_to_accept_challenge)
    }

    @Subscribe
    fun onChallengeDeclinedSuccessfully(event: MainModel.ChallengeDeclinedSuccessfullyEvent) {
        view.showToast(R.string.challenge_declined)
        model.notifyUpdateLists()
        incomingSinglesMatchDialog?.dismiss()
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
        openIncomingSinglesMatchDialog(event.matchRecord)
    }

    @Subscribe
    fun onChallengeSubmittedSuccessfully(event: ChallengeSubmittedSuccessfullyEvent) {
        view.showToast(R.string.match_request_sent)
        model.notifyUpdateLists()
    }

    @Subscribe
    fun onChallengeSubmittedSuccessfully(event: ChallengeSubmitFailedEvent) {
        view.showToast(R.string.match_request_failed)
    }

    private fun updateCurrentFragment(fragment: Fragment) {
        view.withFragmentTransaction {
            if (!fragment.isAdded) {
                replace(R.id.frame_container, fragment)
            }
            commitNow()
        }
    }

    private fun openIncomingSinglesMatchDialog(match: MatchRecord) {
        view.activity?.let {
            incomingSinglesMatchDialog = IncomingSinglesMatchDialog(match, model.getBus())
            incomingSinglesMatchDialog?.show(it)
        }
    }

    private fun openDoublesMatchDialog(match: MatchRecord) {
        view.activity?.let {
            incomingDoublesMatchDialog = IncomingDoublesMatchDialog(match, model.getBus())
            incomingDoublesMatchDialog?.show(it)
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

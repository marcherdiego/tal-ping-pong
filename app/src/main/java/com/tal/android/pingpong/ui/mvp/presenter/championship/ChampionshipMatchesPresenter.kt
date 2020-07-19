package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.PastMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.BaseChampionshipMatchDialog
import com.tal.android.pingpong.ui.dialogs.MatchEditDialog
import com.tal.android.pingpong.ui.dialogs.NewChampionshipDoubleMatchDialog
import com.tal.android.pingpong.ui.dialogs.NewChampionshipSinglesMatchDialog
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipMatchesModel
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipUsersListModel
import com.tal.android.pingpong.ui.mvp.presenter.matcheslist.BaseMatchesListPresenter
import com.tal.android.pingpong.ui.mvp.view.championship.ChampionshipMatchesListView
import org.greenrobot.eventbus.Subscribe

class ChampionshipMatchesPresenter(view: ChampionshipMatchesListView, model: ChampionshipMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<ChampionshipMatchesListView, ChampionshipMatchesModel>(view, model, bus) {

    private var matchEditDialog: MatchEditDialog? = null
    private var newMatchDialog: BaseChampionshipMatchDialog? = null

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(
            PastMatchesAdapter(
                event.matches,
                model.getUserEmail(),
                model.getBus()
            )
        )
    }

    @Subscribe
    fun onMatchClicked(event: PastMatchesAdapter.MatchClickedEvent) {
        view.activity?.let {
            matchEditDialog = MatchEditDialog(event.match, model.getBus())
            matchEditDialog?.show(it)
        }
    }

    @Subscribe
    fun onMatchEditButtonClicked(event: MatchEditDialog.MatchEditButtonClickedEvent) {
        model.editMatch(event.match)
    }

    @Subscribe
    fun onMatchEditDeclinedSuccessfully(event: ChampionshipMatchesModel.MatchEditDeclinedSuccessfullyEvent) {
        view.showToast(R.string.edit_declined)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditDeclineFailed(event: ChampionshipMatchesModel.MatchEditDeclineFailedEvent) {
        view.showToast(R.string.edit_decline_failed)
    }

    @Subscribe
    fun onMatchEditedSuccessfully(event: ChampionshipMatchesModel.MatchEditedSuccessfullyEvent) {
        view.showToast(R.string.edit_request_sent)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onUsersFetchedSuccessfully(event: ChampionshipUsersListModel.UsersFetchedSuccessfullyEvent) {
        model.updateUsers(event.users)
        newMatchDialog?.refreshUsersList()
    }

    @Subscribe
    fun onNewChampionshipMatchButtonClicked(event: ChampionshipMatchesListView.NewChampionshipMatchButtonClickedEvent) {
        val context = view.context ?: return
        val currentUser = model.getCurrentUser() ?: return
        newMatchDialog = if (model.doubles) {
            NewChampionshipSinglesMatchDialog(model.users, currentUser, model.getBus())
        } else {
            NewChampionshipDoubleMatchDialog(model.users, currentUser, model.getBus())
        }
        newMatchDialog?.show(context)
    }

    @Subscribe
    fun onCreateNewChampionshipMatchButtonClicked(event: BaseChampionshipMatchDialog.CreateNewChampionshipMatchButtonClickedEvent) {
        startLoading()
        newMatchDialog?.dismiss()
        model.createMatch(event.match)
    }

    @Subscribe
    fun onMatchCreatedSuccessfully(event: ChampionshipMatchesModel.MatchCreatedSuccessfullyEvent) {
        view.showToast(R.string.match_created_successfully)
        stopLoading()
        model.fetchMatches()
    }

    @Subscribe
    fun onMatchCreationFailed(event: ChampionshipMatchesModel.MatchCreationFailedEvent) {
        view.showToast(R.string.match_request_failed)
        stopLoading()
    }
}

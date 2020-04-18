package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.MatchEditDialog
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.matcheslist.PastMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class PastMatchesPresenter(view: BaseMatchesListView, model: PastMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, PastMatchesModel>(view, model, bus) {

    private var matchEditDialog: MatchEditDialog? = null

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(PastMatchesAdapter(event.matches, model.getUserEmail(), model.getBus()))
    }

    @Subscribe
    fun onMatchClicked(event: PastMatchesAdapter.MatchClickedEvent) {
        view.activity?.let {
            matchEditDialog = MatchEditDialog(event.match, model.getBus(), model.isMyMatchEdit(event.match))
            matchEditDialog?.show(it)
        }
    }

    @Subscribe
    fun onMatchEditButtonClicked(event: MatchEditDialog.MatchEditButtonClickedEvent) {
        model.editMatch(event.match)
    }

    @Subscribe
    fun onMatchAcceptChangesButtonClicked(event: MatchEditDialog.MatchAcceptChangesButtonClickedEvent) {
        model.acceptMatchEdit(event.match)
    }

    @Subscribe
    fun onDeclineMatchEditButtonClicked(event: MatchEditDialog.DeclineMatchEditButtonClickedEvent) {
        model.declineMatchEdit(event.match)
    }

    @Subscribe
    fun onMatchEditDeclinedSuccessfully(event: PastMatchesModel.MatchEditDeclinedSuccessfullyEvent) {
        view.showToast(R.string.edit_declined)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditDeclineFailed(event: PastMatchesModel.MatchEditDeclineFailedEvent) {
        view.showToast(R.string.edit_decline_failed)
    }

    @Subscribe
    fun onMatchEditedSuccessfully(event: PastMatchesModel.MatchEditedSuccessfullyEvent) {
        view.showToast(R.string.edit_request_sent)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditFailed(event: PastMatchesModel.MatchEditFailedEvent) {
        view.showToast(R.string.edit_request_send_failed)
    }

    @Subscribe
    fun onMatchEditAcceptedSuccessfully(event: PastMatchesModel.MatchEditAcceptedSuccessfullyEvent) {
        view.showToast(R.string.edit_accepted)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditAcceptFailed(event: PastMatchesModel.MatchEditAcceptFailedEvent) {
        view.showToast(R.string.network_error_message)
    }
}

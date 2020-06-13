package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.PastMatchesAdapter
import com.tal.android.pingpong.ui.dialogs.MatchEditDialog
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.championship.ReadOnlyMatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.matcheslist.BaseMatchesListPresenter
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class ReadOnlyMatchesPresenter(view: BaseMatchesListView, model: ReadOnlyMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, ReadOnlyMatchesModel>(view, model, bus) {

    private var matchEditDialog: MatchEditDialog? = null

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
    fun onMatchEditDeclinedSuccessfully(event: ReadOnlyMatchesModel.MatchEditDeclinedSuccessfullyEvent) {
        view.showToast(R.string.edit_declined)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditDeclineFailed(event: ReadOnlyMatchesModel.MatchEditDeclineFailedEvent) {
        view.showToast(R.string.edit_decline_failed)
    }

    @Subscribe
    fun onMatchEditedSuccessfully(event: ReadOnlyMatchesModel.MatchEditedSuccessfullyEvent) {
        view.showToast(R.string.edit_request_sent)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditAcceptedSuccessfully(event: ReadOnlyMatchesModel.MatchEditAcceptedSuccessfullyEvent) {
        view.showToast(R.string.edit_accepted)
        model.notifyUpdateLists()
        matchEditDialog?.dismiss()
    }
}

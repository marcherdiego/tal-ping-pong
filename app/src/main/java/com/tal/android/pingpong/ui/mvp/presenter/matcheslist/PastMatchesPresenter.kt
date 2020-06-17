package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.PastMatchesAdapter
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
        startLoading()
        matchEditDialog?.dismiss()
        model.editMatch(event.match)
    }

    @Subscribe
    fun onMatchEditedSuccessfully(event: PastMatchesModel.MatchEditedSuccessfullyEvent) {
        view.showToast(R.string.match_edited)
        model.notifyUpdateLists()
        stopLoading()
    }

    @Subscribe
    fun onMatchEditFailed(event: PastMatchesModel.MatchEditFailedEvent) {
        view.showToast(R.string.edit_request_send_failed)
        stopLoading()
    }
}

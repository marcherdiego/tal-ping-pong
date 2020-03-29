package com.tal.android.pingpong.ui.mvp.presenter.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.ui.adapters.viewholders.MatchViewHolder
import com.tal.android.pingpong.ui.dialogs.ChallengeEditDialog
import com.tal.android.pingpong.ui.dialogs.ChallengeProposalDialog
import com.tal.android.pingpong.ui.mvp.model.matcheslist.BaseMatchesListModel
import com.tal.android.pingpong.ui.mvp.model.matcheslist.PastMatchesModel
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import org.greenrobot.eventbus.Subscribe

class PastMatchesPresenter(view: BaseMatchesListView, model: PastMatchesModel, bus: Bus) :
    BaseMatchesListPresenter<BaseMatchesListView, PastMatchesModel>(view, model, bus) {

    private var challengeEditDialog: ChallengeEditDialog? = null

    @Subscribe
    fun onMatchesFetchedSuccessfully(event: BaseMatchesListModel.MatchesFetchedSuccessfullyEvent) {
        view.setRefreshing(false)
        view.setMatchesAdapter(PastMatchesAdapter(event.matches, model.getUserEmail(), model.getBus()))
    }

    @Subscribe
    fun onMatchClicked(event: PastMatchesAdapter.MatchClickedEvent) {
        view.activity?.let {
            challengeEditDialog = ChallengeEditDialog(event.match, model.getBus(), model.isMyMatchEdit(event.match))
            challengeEditDialog?.show(it)
        }
    }

    @Subscribe
    fun onAcceptMatchEditButtonClicked(event: ChallengeEditDialog.AcceptMatchEditButtonClickedEvent) {
        model.editMatch(event.match)
    }

    @Subscribe
    fun onMatchEditedSuccessfully(event: PastMatchesModel.MatchEditedSuccessfullyEvent) {
        view.showToast(R.string.edit_request_sent)
        challengeEditDialog?.dismiss()
    }

    @Subscribe
    fun onMatchEditFailed(event: PastMatchesModel.MatchEditFailedEvent) {
        view.showToast(R.string.edit_request_send_failed)
    }
}

package com.tal.android.pingpong.ui.mvp.presenter.championship

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.UsersListAdapter
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipUsersListModel
import com.tal.android.pingpong.ui.mvp.view.championship.ChampionshipUsersListView
import org.greenrobot.eventbus.Subscribe

class ChampionshipUsersListPresenter(view: ChampionshipUsersListView, model: ChampionshipUsersListModel) :
    BaseFragmentPresenter<ChampionshipUsersListView, ChampionshipUsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: ChampionshipUsersListModel.UsersFetchedSuccessfullyEvent) {
        model.updateCurrentUser(model.usersList.first { it.userEmail == model.getCurrentUser()?.userEmail })
        view.setUsersListAdapter(
            UsersListAdapter(
                users = model.usersList,
                bus = model.getBus(),
                selectedUser = model.getCurrentUser()
            )
        )
        view.setRefreshing(false)
    }

    @Subscribe
    fun onUsersFetchFailed(event: ChampionshipUsersListModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: ChampionshipUsersListView.RefreshUsersListsEvent) {
        model.fetchUsers()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
    }

    override fun onResume() {
        super.onResume()
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

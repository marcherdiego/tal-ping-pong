package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.mvp.model.ReadOnlyUsersListModel
import com.tal.android.pingpong.ui.mvp.view.ReadOnlyUsersListView
import org.greenrobot.eventbus.Subscribe

class ReadOnlyUsersListPresenter(view: ReadOnlyUsersListView, model: ReadOnlyUsersListModel) :
    BaseFragmentPresenter<ReadOnlyUsersListView, ReadOnlyUsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: ReadOnlyUsersListModel.UsersFetchedSuccessfullyEvent) {
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
    fun onUsersFetchFailed(event: ReadOnlyUsersListModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: ReadOnlyUsersListView.RefreshUsersListsEvent) {
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

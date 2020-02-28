package com.tal.android.talpingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.talpingpong.ui.adapters.UsersListAdapter
import com.tal.android.talpingpong.ui.mvp.model.UsersListModel
import com.tal.android.talpingpong.ui.mvp.view.UsersListView
import org.greenrobot.eventbus.Subscribe

class UsersListPresenter(view: UsersListView, model: UsersListModel) :
    BaseFragmentPresenter<UsersListView, UsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: UsersListModel.UsersFetchedSuccessfullyEvent) {
        view.setUsersListAdapter(UsersListAdapter(event.usersList))
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: UsersListView.RefreshUsersListsEvent) {
        model.fetchUsers()
    }

    override fun onResume() {
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

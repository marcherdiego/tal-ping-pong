package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.dialogs.ChallengeUserDialog
import com.tal.android.pingpong.ui.mvp.model.UsersListModel
import com.tal.android.pingpong.ui.mvp.view.UsersListView
import org.greenrobot.eventbus.Subscribe

class UsersListPresenter(view: UsersListView, model: UsersListModel) :
    BaseFragmentPresenter<UsersListView, UsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: UsersListModel.UsersFetchedSuccessfullyEvent) {
        model.updateCurrentUser(event.usersList.first { it.userEmail == model.getCurrentUser()?.userEmail })
        view.setUsersListAdapter(UsersListAdapter(event.usersList, model.getBus()))
        view.setRefreshing(false)
    }

    @Subscribe
    fun onUsersFetchFailed(event: UsersListModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: UsersListView.RefreshUsersListsEvent) {
        model.fetchUsers()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        view.withActivity {
            val currentUser = model.getCurrentUser() ?: return
            ChallengeUserDialog(currentUser, event.user).show(this, object : ChallengeUserDialog.ChallengeDialogCallback {
                override fun onChallengeUser(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
                    model.challengeUser(user, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                }

                override fun onInvalidTimeSelected() {
                    view.showToast(R.string.invalid_time_in_the_past)
                }
            })
        }
    }

    override fun onResume() {
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

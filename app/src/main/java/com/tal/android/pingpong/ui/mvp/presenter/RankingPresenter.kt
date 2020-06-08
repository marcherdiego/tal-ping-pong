package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.UsersListAdapter
import com.tal.android.pingpong.ui.dialogs.NewSinglesMatchDialog
import com.tal.android.pingpong.ui.mvp.model.RankingModel
import com.tal.android.pingpong.ui.mvp.view.RankingView
import org.greenrobot.eventbus.Subscribe

class RankingPresenter(view: RankingView, model: RankingModel) :
    BaseFragmentPresenter<RankingView, RankingModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: RankingModel.UsersFetchedSuccessfullyEvent) {
        val currentUser = event.usersList.first { it.userEmail == model.getCurrentUser()?.userEmail }
        model.updateCurrentUser(currentUser)
        view.setUsersListAdapter(
            UsersListAdapter(
                users = event.usersList,
                bus = model.getBus(),
                selectedUser = currentUser
            )
        )
        view.setRefreshing(false)
    }

    @Subscribe
    fun onSearchCriteriaChanged(event: RankingView.SearchCriteriaChangedEvent) {
        view.filterUsers(event.criteria ?: return)
    }

    @Subscribe
    fun onUsersFetchFailed(event: RankingModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: RankingView.RefreshUsersListsEvent) {
        view.clearSearchBox()
        model.fetchUsers()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        view.withActivity {
            val currentUser = model.getCurrentUser() ?: return
            NewSinglesMatchDialog(currentUser, event.user, model.getBus()).show(this)
        }
    }

    @Subscribe
    fun onCreateNewSinglesMatchButtonClicked(event: NewSinglesMatchDialog.CreateNewSinglesMatchButtonClickedEvent) {
        model.challengeUser(event.match)
    }

    @Subscribe
    fun onNewSinglesMatchInvalidTimeSelected(event: NewSinglesMatchDialog.NewSinglesMatchInvalidTimeSelectedEvent) {
        view.showToast(R.string.invalid_time_in_the_past)
    }

    override fun onResume() {
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

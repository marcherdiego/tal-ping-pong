package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.dialogs.NewDoublesMatchDialog
import com.tal.android.pingpong.ui.dialogs.NewSinglesMatchDialog
import com.tal.android.pingpong.ui.mvp.model.UsersListModel
import com.tal.android.pingpong.ui.mvp.view.UsersListView
import org.greenrobot.eventbus.Subscribe

class UsersListPresenter(view: UsersListView, model: UsersListModel) :
    BaseFragmentPresenter<UsersListView, UsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: UsersListModel.UsersFetchedSuccessfullyEvent) {
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
    fun onSearchCriteriaChanged(event: UsersListView.SearchCriteriaChangedEvent) {
        view.filterUsers(event.criteria ?: return)
    }

    @Subscribe
    fun onUsersFetchFailed(event: UsersListModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: UsersListView.RefreshUsersListsEvent) {
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
        model.challengeUserSinglesMatch(event.match)
    }

    @Subscribe
    fun onNewSinglesMatchInvalidTimeSelected(event: NewSinglesMatchDialog.NewSinglesMatchInvalidTimeSelectedEvent) {
        view.showToast(R.string.invalid_time_in_the_past)
    }

    @Subscribe
    fun onNewMatchButtonClicked(event: UsersListView.NewButtonClickedEvent) {
        model.showingFabOptions = model.showingFabOptions.not()
        if (model.showingFabOptions) {
            view.expandFabOptions()
        } else {
            view.collapseFabOptions()
        }
    }

    @Subscribe
    fun onOverlayClicked(event: UsersListView.OverlayClickedEvent) {
        collapseFabOptions()
    }

    @Subscribe
    fun onNewSinglesMatchButtonClicked(event: UsersListView.NewSinglesMatchButtonClickedEvent) {
        collapseFabOptions()
        view.showSnackbar(R.string.select_a_rival_from_the_list)
    }

    @Subscribe
    fun onNewDoublesMatchButtonClicked(event: UsersListView.NewDoublesMatchButtonClickedEvent) {
        collapseFabOptions()
        view.withActivity {
            val currentUser = model.getCurrentUser() ?: return
            NewDoublesMatchDialog(model.usersList, currentUser, model.getBus()).show(this)
        }
    }

    @Subscribe
    fun onCreateNewDoublesMatchButtonClicked(event: NewDoublesMatchDialog.CreateNewDoublesMatchButtonClickedEvent) {
        model.challengeUsersDoublesMatch(event.match)
    }

    @Subscribe
    fun onNewDoublesMatchInvalidTimeSelected(event: NewDoublesMatchDialog.NewDoublesMatchInvalidTimeSelectedEvent) {
        view.showToast(R.string.invalid_time_in_the_past)
    }

    @Subscribe
    fun onNewChampionshipButtonClicked(event: UsersListView.NewChampionshipButtonClickedEvent) {
        collapseFabOptions()
    }

    private fun collapseFabOptions() {
        model.showingFabOptions = false
        view.collapseFabOptions()
    }

    override fun onResume() {
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

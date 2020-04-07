package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.dialogs.DoublesMatchDialog
import com.tal.android.pingpong.ui.dialogs.SinglesMatchDialog
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
            SinglesMatchDialog(currentUser, event.user).show(this, object : SinglesMatchDialog.ChallengeDialogCallback {
                override fun onChallengeUser(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
                    model.challengeUser(user, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                }

                override fun onInvalidTimeSelected() {
                    view.showToast(R.string.invalid_time_in_the_past)
                }
            })
        }
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
            DoublesMatchDialog(currentUser).show(this, object : DoublesMatchDialog.ChallengeDialogCallback {
                override fun onChallengeUser(match: MatchRecord, year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) {
                    //model.challengeUser(user, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                }

                override fun onInvalidTimeSelected() {
                    view.showToast(R.string.invalid_time_in_the_past)
                }
            })
        }
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

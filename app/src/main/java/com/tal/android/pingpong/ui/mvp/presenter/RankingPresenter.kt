package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.dialogs.NewSinglesMatchDialog
import com.tal.android.pingpong.ui.mvp.model.RankingModel
import com.tal.android.pingpong.ui.mvp.view.RankingView
import org.greenrobot.eventbus.Subscribe

class RankingPresenter(view: RankingView, model: RankingModel) :
    BaseFragmentPresenter<RankingView, RankingModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: RankingModel.UsersFetchedSuccessfullyEvent) {
        model.updateCurrentUser(event.usersList.first { it.userEmail == model.getCurrentUser()?.userEmail })
        view.setUsersListAdapter(UsersListAdapter(event.usersList, model.getBus()))
        view.setRefreshing(false)
    }

    @Subscribe
    fun onUsersFetchFailed(event: RankingModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: RankingView.RefreshUsersListsEvent) {
        model.fetchUsers()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        view.withActivity {
            val currentUser = model.getCurrentUser() ?: return
            NewSinglesMatchDialog(currentUser, event.user).show(this, object : NewSinglesMatchDialog.ChallengeDialogCallback {
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

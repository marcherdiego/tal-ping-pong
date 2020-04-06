package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.activities.LoginActivity
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.ui.mvp.model.UserProfileModel
import com.tal.android.pingpong.ui.mvp.view.UserProfileView
import com.tal.android.pingpong.utils.DialogFactory
import org.greenrobot.eventbus.Subscribe

class UserProfilePresenter(view: UserProfileView, model: UserProfileModel) :
    BaseFragmentPresenter<UserProfileView, UserProfileModel>(view, model) {

    init {
        view.loadUserBasicInfo(model.user.userImage, model.user.userName, model.user.userEmail)
        view.loadUserMatchesInfo(model.user.matchesWon.toString(), model.user.matchesLost.toString(), model.user.matchesRatio)
        if (model.user.userId == model.getLoggedUser()?.userId) {
            view.showLogoutButton()
        } else {
            view.hideLogoutButton()
        }
    }

    @Subscribe
    fun onLogoutButtonClicked(event: UserProfileView.LogoutButtonClickedEvent) {
        DialogFactory
            .Builder()
            .setMessage(R.string.confirm_logout)
            .setTitle(R.string.app_name)
            .setPositiveButtonText(R.string.logout)
            .setPositiveButtonListener {
                model.logout()
                startActivity(
                    clazz = LoginActivity::class.java,
                    finishCurrent = true
                )
            }
            .setNeutralButtonText(R.string.cancel)
            .build(view.context ?: return)
            .show()
    }

    @Subscribe
    fun onLastTenMatchesFetchedSuccessfully(event: UserProfileModel.LastTenMatchesFetchedSuccessfullyEvent) {
        view.hideMatchesLoaderProgressBar()
        view.setPastMatchesAdapter(PastMatchesAdapter(event.matches, model.user.userEmail, model.getBus()))
    }

    @Subscribe
    fun onLastTenMatchesFetchFailed(event: UserProfileModel.LastTenMatchesFetchFailedEvent) {
        view.hideMatchesLoaderProgressBar()
        view.showNetworkErrorMessage()
    }

    override fun onResume() {
        super.onResume()
        view.showMatchesLoaderProgressBar()
        model.fetchLastTenMatches()
    }
}

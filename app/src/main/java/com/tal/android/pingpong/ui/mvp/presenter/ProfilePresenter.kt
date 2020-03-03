package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.ui.activities.LoginActivity
import com.tal.android.pingpong.ui.mvp.model.ProfileModel
import com.tal.android.pingpong.ui.mvp.view.ProfileView
import org.greenrobot.eventbus.Subscribe

class ProfilePresenter(view: ProfileView, model: ProfileModel) :
    BaseFragmentPresenter<ProfileView, ProfileModel>(view, model) {

    @Subscribe
    fun onLogoutButtonClicked(event: ProfileView.LogoutButtonClickedEvent) {
        model.logout()
        startActivity(
            clazz = LoginActivity::class.java,
            finishCurrent = true
        )
    }
}

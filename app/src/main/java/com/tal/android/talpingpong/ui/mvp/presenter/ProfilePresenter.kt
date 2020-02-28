package com.tal.android.talpingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.talpingpong.ui.mvp.view.ProfileView
import com.tal.android.talpingpong.ui.mvp.model.ProfileModel

class ProfilePresenter(view: ProfileView, model: ProfileModel) :
    BaseFragmentPresenter<ProfileView, ProfileModel>(view, model) {

}

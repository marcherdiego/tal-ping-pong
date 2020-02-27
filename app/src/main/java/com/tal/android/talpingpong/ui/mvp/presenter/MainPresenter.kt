package com.tal.android.talpingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter

import com.tal.android.talpingpong.ui.mvp.model.MainModel
import com.tal.android.talpingpong.ui.mvp.view.MainView

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

}

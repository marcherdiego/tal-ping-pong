package com.tal.android.talpingpong.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.ui.mvp.model.MainModel
import com.tal.android.talpingpong.ui.mvp.presenter.MainPresenter
import com.tal.android.talpingpong.ui.mvp.view.MainView

class MainActivity : BaseActivity<MainPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        presenter = MainPresenter(
            MainView(this),
            MainModel()
        )
    }
}

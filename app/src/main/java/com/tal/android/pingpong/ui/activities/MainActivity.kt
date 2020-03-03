package com.tal.android.pingpong.ui.activities

import android.os.Bundle
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.presenter.MainPresenter
import com.tal.android.pingpong.ui.mvp.view.MainView

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

package com.tal.android.pingpong.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.NewChampionshipModel
import com.tal.android.pingpong.ui.mvp.presenter.NewChampionshipPresenter
import com.tal.android.pingpong.ui.mvp.view.NewChampionshipView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class NewChampionshipActivity : BaseActivity<NewChampionshipPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_championship_activity)

        presenter = NewChampionshipPresenter(
            NewChampionshipView(this),
            NewChampionshipModel(SharedPreferencesUtils(this))
        )
    }
}

package com.tal.android.pingpong.ui.activities

import android.os.Bundle

import com.nerdscorner.mvplib.events.activity.BaseActivity

import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipModel
import com.tal.android.pingpong.ui.mvp.presenter.championship.ChampionshipPresenter
import com.tal.android.pingpong.ui.mvp.view.ChampionshipView

class ChampionshipActivity : BaseActivity<ChampionshipPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.championship_activity)

        val championship = (intent?.getSerializableExtra(CHAMPIONSHIP) as? Championship) ?: return
        presenter = ChampionshipPresenter(
            ChampionshipView(this),
            ChampionshipModel(championship)
        )
    }

    companion object {
        const val CHAMPIONSHIP = "championship"
    }
}

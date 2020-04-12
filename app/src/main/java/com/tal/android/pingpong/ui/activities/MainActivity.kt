package com.tal.android.pingpong.ui.activities

import android.os.Bundle
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.presenter.MainPresenter
import com.tal.android.pingpong.ui.mvp.view.MainView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MainActivity : BaseActivity<MainPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val challengeMatch = intent.getSerializableExtra(EXTRA_CHALLENGE_MATCH) as? MatchRecord
        presenter = MainPresenter(
            MainView(this),
            MainModel(
                sharedPreferences = SharedPreferencesUtils(this),
                challengeMatch = challengeMatch
            )
        )
    }

    companion object {
        const val EXTRA_CHALLENGE_MATCH = "challenge_match"
    }
}

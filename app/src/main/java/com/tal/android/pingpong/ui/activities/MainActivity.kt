package com.tal.android.pingpong.ui.activities

import android.os.Bundle
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.notifications.Constants
import com.tal.android.pingpong.ui.mvp.model.MainModel
import com.tal.android.pingpong.ui.mvp.model.MatchesListModel
import com.tal.android.pingpong.ui.mvp.presenter.MainPresenter
import com.tal.android.pingpong.ui.mvp.view.MainView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MainActivity : BaseActivity<MainPresenter>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val challengeMatch = intent.getSerializableExtra(EXTRA_MATCH) as? MatchRecord
        val screenState = intent.getIntExtra(EXTRA_SCREEN, MainModel.UNSET)
        val selectedTab = intent.getStringExtra(EXTRA_TAB) ?: MatchesListModel.Companion.TabsState.UPCOMING
        val actionType = intent.getStringExtra(ACTION_TYPE) ?: Constants.NONE
        presenter = MainPresenter(
            MainView(this),
            MainModel(
                sharedPreferences = SharedPreferencesUtils(this),
                match = challengeMatch,
                initialScreenState = screenState,
                matchesTabsState = selectedTab,
                actionType = actionType
            )
        )
    }

    companion object {
        const val EXTRA_MATCH = "match"
        const val EXTRA_SCREEN = "screen"
        const val EXTRA_TAB = "tab"
        const val ACTION_TYPE = "action"
    }
}

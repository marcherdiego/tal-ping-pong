package com.tal.android.talpingpong.ui.mvp.view

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.tal.android.talpingpong.R

class MainView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val navigation: BottomNavigationView = activity.findViewById(R.id.bottom_navigation)

    init {
        navigation.setOnNavigationItemSelectedListener {
            bus.post(NavigationItemSelectedEvent(it.itemId))
            true
        }
    }

    class NavigationItemSelectedEvent(val itemId: Int)
}

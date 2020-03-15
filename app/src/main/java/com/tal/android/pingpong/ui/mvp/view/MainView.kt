package com.tal.android.pingpong.ui.mvp.view

import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.tal.android.pingpong.R

class MainView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val navigation: BottomNavigationView = activity.findViewById(R.id.bottom_navigation)

    init {
        navigation.setOnNavigationItemSelectedListener {
            bus.post(NavigationItemSelectedEvent(it.itemId))
            true
        }
    }

    fun setSelectedTab(@IdRes itemId: Int) {
        navigation.selectedItemId = itemId
    }

    class NavigationItemSelectedEvent(val itemId: Int)
}

package com.tal.android.pingpong.ui.mvp.view

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.TabsAdapter

class ChampionshipView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val viewPager: ViewPager? = activity.findViewById(R.id.view_pager)
    private val tabLayout: TabLayout? = activity.findViewById(R.id.tab_layout)

    fun setTabsAdapter(adapter: TabsAdapter) {
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }
}

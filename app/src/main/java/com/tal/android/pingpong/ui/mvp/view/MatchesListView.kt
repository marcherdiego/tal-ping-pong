package com.tal.android.pingpong.ui.mvp.view

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.TabsAdapter

class MatchesListView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val viewPager: ViewPager? = fragment.view?.findViewById(R.id.view_pager)
    private val tabLayout: TabLayout? = fragment.view?.findViewById(R.id.tab_layout)

    fun setTabsAdapter(adapter: TabsAdapter) {
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    fun setSelectedTab(selectedTab: Int) {
        tabLayout?.setScrollPosition(selectedTab, 0f, true)
        viewPager?.currentItem = selectedTab
    }
}

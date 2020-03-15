package com.tal.android.pingpong.ui.mvp.view

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.MatchesTabsAdapter

class MatchesListView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val viewPager: ViewPager? = fragment.view?.findViewById(R.id.view_pager)
    private val tabLayout: TabLayout? = fragment.view?.findViewById(R.id.tab_layout)
    private val newMatchButton: FloatingActionButton? = fragment.view?.findViewById(R.id.new_match_button)

    init {
        newMatchButton?.setOnClickListener {
            bus.post(NewMatchButtonClickedEvent())
        }
    }

    fun setTabsAdapter(adapter: MatchesTabsAdapter) {
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
    }

    class NewMatchButtonClickedEvent
}

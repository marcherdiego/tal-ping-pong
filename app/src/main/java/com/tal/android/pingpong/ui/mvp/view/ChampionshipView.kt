package com.tal.android.pingpong.ui.mvp.view

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.tabs.TabsAdapter

class ChampionshipView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val viewPager: ViewPager = activity.findViewById(R.id.view_pager)
    private val tabLayout: TabLayout = activity.findViewById(R.id.tab_layout)
    private val toolbar: Toolbar = activity.findViewById(R.id.toolbar)

    private val creator: TextView = activity.findViewById(R.id.creator)
    private val date: TextView = activity.findViewById(R.id.date)
    private val members: TextView = activity.findViewById(R.id.members)

    fun setTabsAdapter(adapter: TabsAdapter) {
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    fun setTitle(championshipName: String?) {
        toolbar.title = activity?.getString(R.string.championship_x, championshipName)
    }

    fun setCreator(creator: String?) {
        this.creator.text = activity?.getString(R.string.created_by_x, creator)
    }

    fun setDate(date: String?) {
        this.date.text = date
    }

    fun setMembersCount(members: Int) {
        this.members.text = activity?.resources?.getQuantityString(R.plurals.x_members, members, members)
    }
}

package com.tal.android.pingpong.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MatchesTabsAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val titles: List<String?>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    override fun getPageTitle(position: Int) = titles[position]

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}

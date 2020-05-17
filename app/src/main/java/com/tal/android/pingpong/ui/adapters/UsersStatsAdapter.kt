package com.tal.android.pingpong.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.fragments.UserStatsFragment
import com.tal.android.pingpong.utils.bundle

class UsersStatsAdapter(fragmentActivity: FragmentActivity, vararg users: User) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<Fragment>()

    init {
        users.forEach { user ->
            fragments.add(UserStatsFragment().apply {
                arguments = bundle {
                    putSerializable(UserStatsFragment.USER, user)
                }
            })
        }
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}

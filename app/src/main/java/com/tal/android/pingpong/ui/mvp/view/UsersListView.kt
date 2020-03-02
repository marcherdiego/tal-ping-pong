package com.tal.android.pingpong.ui.mvp.view

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.UsersListAdapter

class UsersListView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val usersList: RecyclerView? = fragment.view?.findViewById(R.id.users_list)
    private val refreshLayout: SwipeRefreshLayout? = activity?.findViewById(R.id.refresh_layout)

    init {
        usersList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshUsersListsEvent())
        }
    }

    fun setUsersListAdapter(adapter: UsersListAdapter) {
        usersList?.adapter = adapter
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshUsersListsEvent
}

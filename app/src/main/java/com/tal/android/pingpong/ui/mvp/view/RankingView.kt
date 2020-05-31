package com.tal.android.pingpong.ui.mvp.view

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.utils.onTextChanged

class RankingView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val usersList: RecyclerView? = fragment.view?.findViewById(R.id.users_list)
    private val refreshLayout: SwipeRefreshLayout? = activity?.findViewById(R.id.refresh_layout)
    private val filterCriteria: EditText? = fragment.view?.findViewById(R.id.filter_criteria)

    init {
        filterCriteria?.onTextChanged {
            bus.post(SearchCriteriaChangedEvent(it?.toString()))
        }
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


    fun clearSearchBox() {
        filterCriteria?.text = null
    }

    fun filterUsers(criteria: String) {
        (usersList?.adapter as? UsersListAdapter)?.filter(criteria)
    }

    class SearchCriteriaChangedEvent(val criteria: String?)
    class RefreshUsersListsEvent
}

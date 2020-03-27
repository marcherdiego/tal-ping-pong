package com.tal.android.pingpong.ui.mvp.view.matcheslist

import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.EmptyAdapter

open class BaseMatchesListView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val emptyListLayout: View? = fragment.view?.findViewById(R.id.empty_list_layout)
    private val challengesList: RecyclerView? = fragment.view?.findViewById(R.id.matches_list)
    private val refreshLayout: SwipeRefreshLayout? = fragment.view?.findViewById(R.id.refresh_layout)

    init {
        challengesList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshMatchesEvent())
        }
    }

    fun setMatchesAdapter(adapter: RecyclerView.Adapter<*>) {
        if (adapter.itemCount == 0) {
            emptyListLayout?.visibility = View.VISIBLE
            challengesList?.adapter = EmptyAdapter()
        } else {
            emptyListLayout?.visibility = View.GONE
            challengesList?.adapter = adapter
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshMatchesEvent
}
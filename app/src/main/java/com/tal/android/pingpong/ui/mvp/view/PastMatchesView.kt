package com.tal.android.pingpong.ui.mvp.view

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.MatchesAdapter

class PastMatchesView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val challengesList: RecyclerView? = fragment.view?.findViewById(R.id.challenges_list)
    private val refreshLayout: SwipeRefreshLayout? = fragment.view?.findViewById(R.id.refresh_layout)

    init {
        challengesList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshMatchesEvent())
        }
    }

    fun setMatchesAdapter(adapter: MatchesAdapter) {
        challengesList?.adapter = adapter
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshMatchesEvent
}

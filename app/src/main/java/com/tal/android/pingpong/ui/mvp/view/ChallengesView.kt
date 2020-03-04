package com.tal.android.pingpong.ui.mvp.view

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.ChallengesAdapter

class ChallengesView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val challengesList: RecyclerView? = fragment.view?.findViewById(R.id.challenges_list)
    private val refreshLayout: SwipeRefreshLayout? = activity?.findViewById(R.id.refresh_layout)

    init {
        challengesList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshChallengesEvent())
        }
    }

    fun setChallengesAdapter(adapter: ChallengesAdapter) {
        challengesList?.adapter = adapter
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshChallengesEvent
}

package com.tal.android.pingpong.ui.mvp.view.championship

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.EmptyAdapter

class ChampionshipStandingsView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val emptyListLayout: View? = fragment.view?.findViewById(R.id.empty_list_layout)
    private val emptyListEmoji: ImageView? = emptyListLayout?.findViewById(R.id.emoji)
    private val emptyListMessage: TextView? = emptyListLayout?.findViewById(R.id.message)

    private val standingsList: RecyclerView? = fragment.view?.findViewById(R.id.standings_list)
    private val refreshLayout: SwipeRefreshLayout? = fragment.view?.findViewById(R.id.refresh_layout)

    init {
        standingsList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshStandingsEvent())
        }
    }

    fun showNetworkErrorMessage() {
        standingsList?.adapter = EmptyAdapter()
        emptyListLayout?.visibility = View.VISIBLE
        emptyListEmoji?.setImageResource(R.drawable.ic_confused)
        emptyListMessage?.setText(R.string.network_error_message)
    }

    fun setStandingsAdapter(adapter: RecyclerView.Adapter<*>) {
        if (adapter.itemCount == 0) {
            emptyListLayout?.visibility = View.VISIBLE
            emptyListEmoji?.setImageResource(R.drawable.ic_sad)
            emptyListMessage?.setText(R.string.no_matches_yet)
            standingsList?.adapter = EmptyAdapter()
        } else {
            emptyListLayout?.visibility = View.GONE
            standingsList?.adapter = adapter
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshStandingsEvent
}

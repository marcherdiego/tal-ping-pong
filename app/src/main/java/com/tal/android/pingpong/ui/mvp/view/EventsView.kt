package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.EmptyAdapter

class EventsView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val emptyListLayout: View? = fragment.view?.findViewById(R.id.empty_list_layout)
    private val emptyListEmoji: ImageView? = emptyListLayout?.findViewById(R.id.emoji)
    private val emptyListMessage: TextView? = emptyListLayout?.findViewById(R.id.message)

    private val championshipsList: RecyclerView? = fragment.view?.findViewById(R.id.championships_list)
    private val refreshLayout: SwipeRefreshLayout? = fragment.view?.findViewById(R.id.refresh_layout)

    init {
        championshipsList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )

        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshChampionshipsEvent())
        }
    }

    fun showNetworkErrorMessage() {
        championshipsList?.adapter = EmptyAdapter()
        emptyListLayout?.visibility = View.VISIBLE
        emptyListEmoji?.setImageResource(R.drawable.ic_confused)
        emptyListMessage?.setText(R.string.network_error_message)
    }

    fun setChampionshipsAdapter(adapter: RecyclerView.Adapter<*>) {
        if (adapter.itemCount == 0) {
            emptyListLayout?.visibility = View.VISIBLE
            emptyListEmoji?.setImageResource(R.drawable.ic_sad)
            emptyListMessage?.setText(R.string.no_championships_yet)
            championshipsList?.adapter = EmptyAdapter()
        } else {
            emptyListLayout?.visibility = View.GONE
            championshipsList?.adapter = adapter
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    class RefreshChampionshipsEvent
}

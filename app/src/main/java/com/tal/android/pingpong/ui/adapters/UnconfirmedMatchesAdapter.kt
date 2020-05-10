package com.tal.android.pingpong.ui.adapters

import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.viewholders.BaseMatchViewHolder

class UnconfirmedMatchesAdapter(
    private val matches: List<Match>,
    myEmail: String?,
    private val bus: Bus
) : UpcomingMatchesAdapter(matches, myEmail) {

    override fun onBindViewHolder(holder: BaseMatchViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val match = matches[position].match ?: return
        holder.itemView.setOnClickListener {
            bus.post(UpcomingMatchClickedEvent(match))
        }
    }

    class UpcomingMatchClickedEvent(val matchRecord: MatchRecord)
}

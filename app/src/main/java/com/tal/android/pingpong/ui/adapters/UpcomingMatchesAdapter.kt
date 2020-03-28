package com.tal.android.pingpong.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.ui.adapters.viewholders.MatchViewHolder

class UpcomingMatchesAdapter(private val matches: List<Match>, private val myEmail: String?) : RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.upcoming_match_item_row, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        val match = matches[position]
        return when (match.match?.local?.userEmail) {
            myEmail -> MatchViewHolder.VIEW_TYPE_LOCAL
            else -> MatchViewHolder.VIEW_TYPE_VISITOR
        }
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        with(holder) {
            val match = matches[position]
            bindBasicMatchData(match.match ?: return)

            matchesHistory?.removeAllViews()
            if (match.matchesHistory.isEmpty()) {
                matchesHistory?.addView(TextView(itemView.context).apply {
                    setText(R.string.no_match_history)
                })
            } else {
                match.matchesHistory.forEach { pastMatch ->
                    LayoutInflater
                        .from(itemView.context)
                        .inflate(
                            if (pastMatch.myVictory(myEmail)) {
                                R.layout.match_record_win_view
                            } else {
                                R.layout.match_record_lose_view
                            },
                            matchesHistory,
                            true
                        )
                }
            }
        }
    }
}

package com.tal.android.pingpong.ui.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.ui.adapters.viewholders.MatchViewHolder

class PastMatchesAdapter(private val matches: List<Match>, private val myEmail: String?) :
    RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    if (viewType == MatchViewHolder.VIEW_TYPE_LOCAL) {
                        R.layout.past_match_local_item_row
                    } else {
                        R.layout.past_match_visitor_item_row
                    },
                    parent,
                    false
                )
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

            val localPlayerScore = match.match?.localScore ?: 0
            val visitorPlayerScore = match.match?.visitorScore ?: 0
            localScore?.text = localPlayerScore.toString()
            visitorScore?.text = visitorPlayerScore.toString()

            if (localPlayerScore > visitorPlayerScore) {
                localScore?.setTypeface(localScore.typeface, Typeface.BOLD)
                visitorScore?.setTypeface(visitorScore.typeface, Typeface.NORMAL)
            } else {
                localScore?.setTypeface(localScore.typeface, Typeface.NORMAL)
                visitorScore?.setTypeface(visitorScore.typeface, Typeface.BOLD)
            }

            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (match.match?.myVictory(myEmail) == true) {
                        R.color.victory_background_color
                    } else {
                        R.color.defeat_background_color
                    }
                )
            )
        }
    }
}

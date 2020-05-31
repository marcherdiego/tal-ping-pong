package com.tal.android.pingpong.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.ui.adapters.viewholders.BaseMatchViewHolder
import com.tal.android.pingpong.ui.adapters.viewholders.DoublesMatchViewHolder
import java.lang.IllegalArgumentException

open class UpcomingMatchesAdapter(private val matches: List<Match>, private val myEmail: String?) :
    RecyclerView.Adapter<BaseMatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMatchViewHolder {
        return when (viewType) {
            VIEW_TYPE_SINGLES -> BaseMatchViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.upcoming_singles_match_item_row, parent, false)
            )
            VIEW_TYPE_DOUBLES -> DoublesMatchViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.upcoming_doubles_match_item_row, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val match = matches[position].match
        return if (match?.isSinglesMatch() == true) {
            VIEW_TYPE_SINGLES
        } else {
            VIEW_TYPE_DOUBLES
        }
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: BaseMatchViewHolder, position: Int) {
        with(holder) {
            val match = matches[position]
            val matchRecord = match.match ?: return
            bindBasicMatchData(matchRecord, myEmail)

            if (this is DoublesMatchViewHolder && matchRecord.confirmed == true) {
                localCompanionUserImage.alpha = getUserImageAlpha(true)
                visitorUserImage.alpha = getUserImageAlpha(true)
                visitorCompanionUserImage.alpha = getUserImageAlpha(true)

                //Hide confirmed icon, it's redundant
                localUserConfirmedImage?.visibility = getConfirmedVisibility(false)
                localCompanionUserConfirmedImage?.visibility = getConfirmedVisibility(false)
                visitorUserConfirmedImage?.visibility = getConfirmedVisibility(false)
                visitorCompanionUserConfirmedImage?.visibility = getConfirmedVisibility(false)
            }

            matchesHistory?.let { matchesHistory ->
                matchesHistory.removeAllViews()
                if (match.matchesHistory.isEmpty()) {
                    matchesHistory.addView(TextView(itemView.context).apply {
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

    companion object {
        const val VIEW_TYPE_SINGLES = 1
        const val VIEW_TYPE_DOUBLES = 2
    }
}

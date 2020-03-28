package com.tal.android.pingpong.ui.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.ui.adapters.viewholders.MatchViewHolder

class PastMatchesAdapter(private val matches: List<Match>, private val myEmail: String?, private val bus: Bus) :
    RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.past_match_item_row, parent, false)
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
            val matchRecord = match.match ?: return

            bindBasicMatchData(matchRecord)

            val localPlayerScore = matchRecord.localScore
            val visitorPlayerScore = matchRecord.visitorScore

            if (localPlayerScore > visitorPlayerScore) {
                localScore?.setTypeface(localScore.typeface, Typeface.BOLD)
                visitorScore?.setTypeface(visitorScore.typeface, Typeface.NORMAL)
            } else {
                localScore?.setTypeface(localScore.typeface, Typeface.NORMAL)
                visitorScore?.setTypeface(visitorScore.typeface, Typeface.BOLD)
            }

            if (matchRecord.hasRequestedChanges == true) {
                // User has requested changes
                confirmedLabel?.setText(R.string.changes_requested)
                confirmedIcon?.setImageResource(R.drawable.ic_warning)

                val requestedLocalScore = matchRecord.requestedLocalScore
                if (requestedLocalScore == localPlayerScore) {
                    // Local score unchanged
                    oldLocalScore?.visibility = View.GONE
                    localScore?.text = localPlayerScore.toString()
                } else {
                    oldLocalScore?.visibility = View.VISIBLE
                    oldLocalScore?.text = localPlayerScore.toString()
                    localScore?.text = matchRecord.requestedLocalScore.toString()
                }

                val requestedVisitorScore = matchRecord.requestedVisitorScore
                if (requestedVisitorScore == visitorPlayerScore) {
                    // Visitor score unchanged
                    oldVisitorScore?.visibility = View.GONE
                    visitorScore?.text = visitorPlayerScore.toString()
                } else {
                    oldVisitorScore?.visibility = View.VISIBLE
                    oldVisitorScore?.text = visitorPlayerScore.toString()
                    visitorScore?.text = matchRecord.requestedVisitorScore.toString()
                }
            } else {
                // No changes requested
                oldLocalScore?.visibility = View.GONE
                oldVisitorScore?.visibility = View.GONE
                localScore?.text = localPlayerScore.toString()
                visitorScore?.text = visitorPlayerScore.toString()
                if (matchRecord.confirmed == true) {
                    confirmedLabel?.setText(R.string.confirmed)
                    confirmedIcon?.setImageResource(R.drawable.ic_verified)
                } else {
                    confirmedLabel?.setText(R.string.not_played)
                    confirmedIcon?.setImageResource(R.drawable.ic_close)
                }
            }

            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (matchRecord.hasRequestedChanges == true) {
                        R.color.changes_requested_background_color
                    } else {
                        if (matchRecord.confirmed == true) {
                            if (matchRecord.myVictory(myEmail)) {
                                R.color.victory_background_color
                            } else {
                                R.color.defeat_background_color
                            }
                        } else {
                            R.color.match_not_played
                        }
                    }
                )
            )
            itemView.setOnClickListener {
                bus.post(MatchClickedEvent(matchRecord))
            }
        }
    }

    class MatchClickedEvent(val match: MatchRecord)
}

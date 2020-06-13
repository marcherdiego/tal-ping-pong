package com.tal.android.pingpong.ui.adapters.recyclerview

import android.content.Context
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
import com.tal.android.pingpong.ui.adapters.viewholders.BaseMatchViewHolder
import com.tal.android.pingpong.ui.adapters.viewholders.DoublesMatchViewHolder
import java.lang.IllegalArgumentException

class PastMatchesAdapter(private val matches: List<Match>, private val userEmail: String?, private val bus: Bus) :
    RecyclerView.Adapter<BaseMatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMatchViewHolder {
        return when (viewType) {
            UpcomingMatchesAdapter.VIEW_TYPE_SINGLES -> BaseMatchViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.past_singles_match_item_row, parent, false)
            )
            UpcomingMatchesAdapter.VIEW_TYPE_DOUBLES -> DoublesMatchViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.past_doubles_match_item_row, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val match = matches[position].match
        return if (match?.isSinglesMatch() == true) {
            UpcomingMatchesAdapter.VIEW_TYPE_SINGLES
        } else {
            UpcomingMatchesAdapter.VIEW_TYPE_DOUBLES
        }
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: BaseMatchViewHolder, position: Int) {
        with(holder) {
            val context = itemView.context
            val match = matches[position]
            val matchRecord = match.match ?: return

            bindBasicMatchData(matchRecord, userEmail)

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
                confirmedLabel?.text = getChangesRequesterName(context, matchRecord)
                confirmedIcon?.setImageResource(R.drawable.ic_warning)

                val requestedLocalScore = matchRecord.requestedLocalScore
                if (requestedLocalScore == localPlayerScore) {
                    // Local score unchanged
                    oldLocalScore?.visibility = View.INVISIBLE
                    localScore?.text = localPlayerScore.toString()
                } else {
                    oldLocalScore?.visibility = View.VISIBLE
                    oldLocalScore?.text = localPlayerScore.toString()
                    localScore?.text = matchRecord.requestedLocalScore.toString()
                }

                val requestedVisitorScore = matchRecord.requestedVisitorScore
                if (requestedVisitorScore == visitorPlayerScore) {
                    // Visitor score unchanged
                    oldVisitorScore?.visibility = View.INVISIBLE
                    visitorScore?.text = visitorPlayerScore.toString()
                } else {
                    oldVisitorScore?.visibility = View.VISIBLE
                    oldVisitorScore?.text = visitorPlayerScore.toString()
                    visitorScore?.text = matchRecord.requestedVisitorScore.toString()
                }
            } else {
                // No changes requested
                oldLocalScore?.visibility = View.INVISIBLE
                oldVisitorScore?.visibility = View.INVISIBLE
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
                    context,
                    if (matchRecord.hasRequestedChanges == true) {
                        R.color.changes_requested_background_color
                    } else {
                        if (matchRecord.confirmed == true) {
                            if (matchRecord.myVictory(userEmail)) {
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

    private fun getChangesRequesterName(context: Context, matchRecord: MatchRecord): String? {
        val local = matchRecord.local ?: return null
        val visitor = matchRecord.visitor ?: return null
        val (myUser, rivalUser) = if (local.userEmail == userEmail) {
            Pair(local, visitor)
        } else {
            Pair(visitor, local)
        }
        return context.getString(
            R.string.changes_requested_by_x,
            if (matchRecord.changeRequestUserId == myUser.userId) {
                context.getString(R.string.me)
            } else {
                rivalUser.firstName()
            }
        )
    }

    class MatchClickedEvent(val match: MatchRecord)
}

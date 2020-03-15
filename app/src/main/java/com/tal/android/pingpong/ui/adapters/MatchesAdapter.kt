package com.tal.android.pingpong.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.GlideUtils

class MatchesAdapter(private val matches: List<Match>, private val myEmail: String?) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    if (viewType == VIEW_TYPE_LOCAL) {
                        R.layout.match_local_item_row
                    } else {
                        R.layout.match_visitor_item_row
                    },
                    parent,
                    false
                )
        )
    }

    override fun getItemViewType(position: Int): Int {
        val match = matches[position]
        return when (match.match?.local?.userEmail) {
            myEmail -> VIEW_TYPE_LOCAL
            else -> VIEW_TYPE_VISITOR
        }
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]
        val context = holder.itemView.context
        val rival = if (holder.itemViewType == VIEW_TYPE_LOCAL) {
            match.match?.visitor
        } else {
            match.match?.local
        }
        with(holder) {
            matchesHistory.removeAllViews()
            GlideUtils.loadImage(match.match?.local?.userImage, localImage, R.drawable.ic_incognito, true)
            userName.text = rival?.userName
            match.matchesHistory?.forEach { matchRecord ->
                LayoutInflater
                    .from(context)
                    .inflate(
                        if (matchRecord.myVictory(myEmail)) {
                            R.layout.match_record_win_view
                        } else {
                            R.layout.match_record_lose_view
                        },
                        matchesHistory,
                        true
                    )
            } ?: run {
                matchesHistory.addView(TextView(context).apply {
                    setText(R.string.no_match_history)
                })
            }
            matchDate.text = DateUtils.formatDate(match.match?.matchDate)
            GlideUtils.loadImage(match.match?.visitor?.userImage, visitorImage, R.drawable.ic_incognito, true)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val localImage: ImageView = itemView.findViewById(R.id.local_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val matchesHistory: LinearLayout = itemView.findViewById(R.id.matches_history)
        val matchDate: TextView = itemView.findViewById(R.id.match_date)
        val visitorImage: ImageView = itemView.findViewById(R.id.visitor_image)
    }

    companion object {
        private const val VIEW_TYPE_LOCAL = 1
        private const val VIEW_TYPE_VISITOR = 2
    }
}

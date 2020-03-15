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

class PastMatchesAdapter(private val matches: List<Match>, private val myEmail: String?) :
    RecyclerView.Adapter<PastMatchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    if (viewType == VIEW_TYPE_LOCAL) {
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
            myEmail -> VIEW_TYPE_LOCAL
            else -> VIEW_TYPE_VISITOR
        }
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matches[position]
        val rival = if (holder.itemViewType == VIEW_TYPE_LOCAL) {
            match.match?.visitor
        } else {
            match.match?.local
        }
        with(holder) {
            GlideUtils.loadImage(match.match?.local?.userImage, localImage, R.drawable.ic_incognito, true)
            userName.text = rival?.userName
            matchDate.text = DateUtils.formatDate(match.match?.matchDate)
            GlideUtils.loadImage(match.match?.visitor?.userImage, visitorImage, R.drawable.ic_incognito, true)

            localScore.text = match.match?.localScore?.toString()
            visitorScore.text = match.match?.visitorScore?.toString()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val localImage: ImageView = itemView.findViewById(R.id.local_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val matchDate: TextView = itemView.findViewById(R.id.match_date)
        val visitorImage: ImageView = itemView.findViewById(R.id.visitor_image)

        val localScore: TextView = itemView.findViewById(R.id.local_score)
        val visitorScore: TextView = itemView.findViewById(R.id.visitor_score)
    }

    companion object {
        private const val VIEW_TYPE_LOCAL = 1
        private const val VIEW_TYPE_VISITOR = 2
    }
}

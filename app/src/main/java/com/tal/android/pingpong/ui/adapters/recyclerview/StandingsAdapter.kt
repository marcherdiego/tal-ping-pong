package com.tal.android.pingpong.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.StandingsAdapter.ViewHolder
import android.widget.TextView
import android.widget.ImageView
import com.tal.android.pingpong.domain.Standing
import com.tal.android.pingpong.utils.load

class StandingsAdapter(private var doublesChampionship: Boolean, private val standings: List<Standing>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    if (viewType == HEADER) {
                        R.layout.standings_header_row
                    } else {
                        R.layout.standings_item_row
                    },
                    parent,
                    false
                )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER
        } else {
            ITEM
        }
    }

    override fun getItemCount() = standings.size + 1

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        if (holder.itemViewType == ITEM) {
            val standing = standings[index - 1]
            with(holder) {
                position.text = index.toString()
                if (doublesChampionship) {
                    user2Avatar?.visibility = View.VISIBLE
                    user2Separator?.visibility = View.VISIBLE
                    user2Name?.visibility = View.VISIBLE
                    avatar.load(
                        standing.team?.user1?.userImage,
                        R.drawable.ic_incognito,
                        true
                    )
                    user2Avatar.load(
                        standing.team?.user2?.userImage,
                        R.drawable.ic_incognito,
                        true
                    )
                    name.text = standing.team?.user1?.userName
                    user2Name?.text = standing.team?.user2?.userName
                } else {
                    user2Avatar?.visibility = View.GONE
                    user2Separator?.visibility = View.GONE
                    user2Name?.visibility = View.GONE
                    avatar.load(
                        standing.user?.userImage,
                        R.drawable.ic_incognito,
                        true
                    )
                    name.text = standing.user?.userName
                }
                played.text = standing.played.toString()
                won.text = standing.won.toString()
                lost.text = standing.lost.toString()
                goalsScored.text = standing.goalsScored.toString()
                goalsAgainst.text = standing.goalsAgainst.toString()
                goalsDifference.text = standing.getGoalsDifference().toString()
                points.text = standing.points.toString()
            }
        } else if (holder.itemViewType == HEADER) {
            holder.position.setText(
                if (doublesChampionship) {
                    R.string.team
                } else {
                    R.string.player
                }
            )
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val position: TextView = itemView.findViewById(R.id.position)
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val user2Avatar: ImageView? = itemView.findViewById(R.id.user_2_avatar)
        val user2Separator: View? = itemView.findViewById(R.id.user_2_separator)
        val name: TextView = itemView.findViewById(R.id.name)
        val user2Name: TextView? = itemView.findViewById(R.id.user_2_name)
        val played: TextView = itemView.findViewById(R.id.played)
        val won: TextView = itemView.findViewById(R.id.won)
        val lost: TextView = itemView.findViewById(R.id.lost)
        val goalsScored: TextView = itemView.findViewById(R.id.goals_scored)
        val goalsAgainst: TextView = itemView.findViewById(R.id.goals_against)
        val goalsDifference: TextView = itemView.findViewById(R.id.goals_difference)
        val points: TextView = itemView.findViewById(R.id.points)
    }

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }
}

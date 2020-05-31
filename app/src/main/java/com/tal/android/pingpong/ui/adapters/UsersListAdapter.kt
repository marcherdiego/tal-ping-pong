package com.tal.android.pingpong.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.UsersListAdapter.ViewHolder
import com.tal.android.pingpong.utils.load

class UsersListAdapter(
    users: List<User>, private val bus: Bus, private val selectable: Boolean = false, private var selectedUser: User? = null
) : RecyclerView.Adapter<ViewHolder>(), Filterable<User> {

    override var originalList = users.toMutableList()
    override var filteredList = users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.users_list_item_row, parent, false)
        )
    }

    override fun getItemCount() = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = filteredList[position]
        val context = holder.itemView.context
        with(holder) {
            userImage.load(user.userImage, R.drawable.ic_incognito, true)
            userName.text = user.userName
            userEmail.text = user.userEmail
            userRank.text = context.getString(R.string.user_rank, user.userRank)

            if (user.champion == true) {
                userChampionTrophy.setImageResource(R.drawable.ic_first_place_trophy)
            } else {
                userChampionTrophy.setImageDrawable(null)
            }

            when (user.userRank) {
                RANK_FIRST_PLACE -> userTrophy.setImageResource(R.drawable.ic_gold_medal)
                RANK_SECOND_PLACE -> userTrophy.setImageResource(R.drawable.ic_silver_medal)
                RANK_THIRD_PLACE -> userTrophy.setImageResource(R.drawable.ic_bronze_medal)
                else -> userTrophy.setImageDrawable(null)
            }

            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (user == selectedUser) {
                        R.color.match_selected
                    } else {
                        R.color.match_unselected
                    }
                )
            )
            itemView.setOnClickListener {
                if (selectable) {
                    selectedUser = user
                }
                notifyDataSetChanged()
                bus.post(UserClickedEvent(user))
            }
        }
    }

    override fun filter(criteria: String) {
        super.filter(criteria)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userEmail: TextView = itemView.findViewById(R.id.user_email)
        val userRank: TextView = itemView.findViewById(R.id.user_rank)
        val userTrophy: ImageView = itemView.findViewById(R.id.user_trophy)
        val userChampionTrophy: ImageView = itemView.findViewById(R.id.user_champion_trophy)
    }

    class UserClickedEvent(val user: User)

    companion object {
        private const val RANK_FIRST_PLACE = 1
        private const val RANK_SECOND_PLACE = 2
        private const val RANK_THIRD_PLACE = 3
    }
}

package com.tal.android.talpingpong.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.domain.User
import com.tal.android.talpingpong.ui.adapters.UsersListAdapter.ViewHolder

class UsersListAdapter(private val users: List<User>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.users_list_item_row, parent, false)
        )
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        with(holder) {
            val matchesWon = user.matchesWon ?: 0
            val matchesLost = user.matchesLost ?: 0
            val matchesRatio = matchesWon.toFloat() / matchesLost.toFloat()
            Glide
                .with(holder.itemView.context)
                .load(user.userImage)
                .into(userImage)
            userName.text = user.userName
            userEmail.text = user.userEmail
            userStats.text = "W: $matchesWon / L: $matchesLost | Ratio: $matchesRatio"
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userEmail: TextView = itemView.findViewById(R.id.user_email)
        val userStats: TextView = itemView.findViewById(R.id.user_stats)
    }
}

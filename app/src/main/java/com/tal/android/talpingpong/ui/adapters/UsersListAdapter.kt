package com.tal.android.talpingpong.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.domain.User
import com.tal.android.talpingpong.ui.adapters.UsersListAdapter.ViewHolder
import com.tal.android.talpingpong.utils.GlideUtils

class UsersListAdapter(private val users: List<User>, private val bus: Bus) : RecyclerView.Adapter<ViewHolder>() {

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
        val context = holder.itemView.context
        with(holder) {
            GlideUtils.loadImage(user.userImage, userImage, R.drawable.ic_incognito, true)
            userName.text = user.userName
            userEmail.text = user.userEmail
            userRank.text = context.getString(R.string.user_rank, user.userRank)
            itemView.setOnClickListener {
                bus.post(UserClickedEvent(user))
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val userEmail: TextView = itemView.findViewById(R.id.user_email)
        val userRank: TextView = itemView.findViewById(R.id.user_rank)
    }

    class UserClickedEvent(val user: User)
}

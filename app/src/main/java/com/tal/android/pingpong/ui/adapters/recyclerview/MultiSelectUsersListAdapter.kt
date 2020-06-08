package com.tal.android.pingpong.ui.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.recyclerview.MultiSelectUsersListAdapter.ViewHolder
import com.tal.android.pingpong.ui.adapters.interfaces.Filterable
import com.tal.android.pingpong.utils.load

class MultiSelectUsersListAdapter(
    users: List<User>,
    private val currentUser: User,
    private val bus: Bus,
    private var selectedUsers: MutableList<User>
) : RecyclerView.Adapter<ViewHolder>(), Filterable<User> {

    override var originalList = users.toMutableList()
    override var filteredList = users

    init {
        selectedUsers.add(currentUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.multi_selectable_users_list_item_row, parent, false)
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

            userSelected.isChecked = selectedUsers.contains(user)
            if (user == currentUser) {
                userSelected.isEnabled = false
                userSelected.setOnClickListener(null)
                itemView.setOnClickListener(null)
            } else {
                val rowClickListener = View.OnClickListener {
                    if (selectedUsers.contains(user)) {
                        selectedUsers.remove(user)
                    } else {
                        selectedUsers.add(user)
                    }
                    userSelected.isChecked = selectedUsers.contains(user)
                    bus.post(
                        SelectedUsersChangedEvent(
                            selectedUsers
                        )
                    )
                }
                userSelected.setOnClickListener(rowClickListener)
                itemView.setOnClickListener(rowClickListener)
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
        val userSelected: CheckBox = itemView.findViewById(R.id.user_selected)
    }

    class SelectedUsersChangedEvent(val selectedUsers: List<User>)
}

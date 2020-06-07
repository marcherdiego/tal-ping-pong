package com.tal.android.pingpong.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.ChampionshipsAdapter.ViewHolder
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.domain.Championship
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.load

class ChampionshipsAdapter(private val championships: List<Championship>, private val bus: Bus) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.championships_list_item_row, parent, false)
        )
    }

    override fun getItemCount() = championships.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val championship = championships[position]
        with(holder) {
            championshipImage.load(championship.championshipImage, R.drawable.ic_championship, true)
            championshipName.text = championship.championshipName
            championshipCreatorUserEmail.text = championship.creator?.userEmail
            championshipDate.text = DateUtils.formatDate(championship.championshipDate)
            val usersCount = championship.usersCount ?: 0
            championshipUsersCount.text = itemView.context.resources.getQuantityString(R.plurals.x_members, usersCount, usersCount)
            itemView.setOnClickListener {
                bus.post(ChampionshipClickedEvent(championship))
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val championshipImage: ImageView = itemView.findViewById(R.id.championship_image)
        val championshipName: TextView = itemView.findViewById(R.id.championship_name)
        val championshipCreatorUserEmail: TextView = itemView.findViewById(R.id.championship_creator_user_email)
        val championshipDate: TextView = itemView.findViewById(R.id.championship_date)
        val championshipUsersCount: TextView = itemView.findViewById(R.id.championship_users_count)
    }

    class ChampionshipClickedEvent(val championship: Championship)
}

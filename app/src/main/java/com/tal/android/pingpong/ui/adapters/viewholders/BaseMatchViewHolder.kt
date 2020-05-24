package com.tal.android.pingpong.ui.adapters.viewholders

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.ALPHA_30
import com.tal.android.pingpong.utils.DEFAULT_ALPHA
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.load

open class BaseMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Common fields
    private val localUserName: TextView = itemView.findViewById(R.id.local_user_name)
    private val localUserImage: ImageView = itemView.findViewById(R.id.local_user_image)
    private val visitorUserName: TextView = itemView.findViewById(R.id.visitor_user_name)
    private val matchDate: TextView = itemView.findViewById(R.id.match_date)
    val visitorUserImage: ImageView = itemView.findViewById(R.id.visitor_image)

    // Upcoming matches fields
    val matchesHistory: LinearLayout? = itemView.findViewById(R.id.matches_history)

    // Past matches fields
    val confirmedIcon: ImageView? = itemView.findViewById(R.id.confirmed_icon)
    val localScore: TextView? = itemView.findViewById(R.id.local_score)
    val oldLocalScore: TextView? = itemView.findViewById(R.id.old_local_score)
    val visitorScore: TextView? = itemView.findViewById(R.id.visitor_score)
    val oldVisitorScore: TextView? = itemView.findViewById(R.id.old_visitor_score)
    val confirmedLabel: TextView? = itemView.findViewById(R.id.confirmed_label)

    init {
        oldLocalScore?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        oldVisitorScore?.let {
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    open fun bindBasicMatchData(match: MatchRecord, myEmail: String?) {
        localUserName.text = match.local?.userName
        localUserImage.load(match.local?.userImage, R.drawable.ic_incognito, true)

        visitorUserName.text = match.visitor?.userName
        visitorUserImage.load(match.visitor?.userImage, R.drawable.ic_incognito, true, getUserImageAlpha(match.confirmed))

        matchDate.text = DateUtils.formatDate(match.matchDate)
    }

    fun getUserImageAlpha(confirmed: Boolean?) = if (confirmed == true) {
        DEFAULT_ALPHA
    } else {
        ALPHA_30
    }
}

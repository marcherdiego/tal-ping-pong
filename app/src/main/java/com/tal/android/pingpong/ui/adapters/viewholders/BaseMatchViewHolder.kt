package com.tal.android.pingpong.ui.adapters.viewholders

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.DateUtils
import com.tal.android.pingpong.utils.load

open class BaseMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Common fields
    private val mainContainer: ConstraintLayout = itemView.findViewById(R.id.main_container)
    private val localImage: ImageView = itemView.findViewById(R.id.local_image)
    private val visitorUserName: TextView = itemView.findViewById(R.id.visitor_user_name)
    private val matchDate: TextView = itemView.findViewById(R.id.match_date)
    private val visitorImage: ImageView = itemView.findViewById(R.id.visitor_image)

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
        val isLocalMatch = match.local?.userEmail == myEmail
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainContainer)
        if (isLocalMatch) {
            constraintSet.clear(R.id.my_user_name, ConstraintSet.END)
            constraintSet.clear(R.id.visitor_user_name, ConstraintSet.START)
            constraintSet.clear(R.id.matches_history, ConstraintSet.START)

            constraintSet.connect(R.id.my_user_name, ConstraintSet.START, R.id.main_container, ConstraintSet.START)
            constraintSet.connect(R.id.visitor_user_name, ConstraintSet.END, R.id.main_container, ConstraintSet.END)
            constraintSet.connect(R.id.matches_history, ConstraintSet.END, R.id.main_container, ConstraintSet.END)
        } else {
            constraintSet.clear(R.id.my_user_name, ConstraintSet.START)
            constraintSet.clear(R.id.visitor_user_name, ConstraintSet.END)
            constraintSet.clear(R.id.matches_history, ConstraintSet.END)

            constraintSet.connect(R.id.my_user_name, ConstraintSet.END, R.id.main_container, ConstraintSet.END)
            constraintSet.connect(R.id.visitor_user_name, ConstraintSet.START, R.id.main_container, ConstraintSet.START)
            constraintSet.connect(R.id.matches_history, ConstraintSet.START, R.id.main_container, ConstraintSet.START)
        }

        val rival = if (isLocalMatch) {
            match.visitor
        } else {
            match.local
        }
        constraintSet.applyTo(mainContainer)

        localImage.load(match.local?.userImage, R.drawable.ic_incognito, true)
        visitorUserName.text = rival?.userName
        matchDate.text = DateUtils.formatDate(match.matchDate)
        visitorImage.load(match.visitor?.userImage, R.drawable.ic_incognito, true)
    }
}

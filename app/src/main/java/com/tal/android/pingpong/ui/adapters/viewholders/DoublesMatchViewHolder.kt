package com.tal.android.pingpong.ui.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.load

class DoublesMatchViewHolder(itemView: View) : SinglesMatchViewHolder(itemView) {
    private val companionsMainContainer: ConstraintLayout = itemView.findViewById(R.id.companions_main_container)
    private val localCompanionImage: ImageView = itemView.findViewById(R.id.local_companion_image)
    private val localCompanionUserName: TextView = itemView.findViewById(R.id.local_companion_user_name)
    private val visitorCompanionUserName: TextView = itemView.findViewById(R.id.visitor_companion_user_name)
    private val visitorCompanionImage: ImageView = itemView.findViewById(R.id.visitor_companion_image)

    override fun bindBasicMatchData(match: MatchRecord, myEmail: String?) {
        super.bindBasicMatchData(match, myEmail)

        val isLocalMatch = match.local?.userEmail == myEmail
        val constraints = ConstraintSet()
        constraints.clone(companionsMainContainer)
        if (isLocalMatch) {
            constraints.clear(R.id.local_companion_user_name, ConstraintSet.END)
            constraints.clear(R.id.visitor_companion_user_name, ConstraintSet.START)

            constraints.connect(R.id.local_companion_user_name, ConstraintSet.START, R.id.companions_main_container, ConstraintSet.START)
            constraints.connect(R.id.visitor_companion_user_name, ConstraintSet.END, R.id.companions_main_container, ConstraintSet.END)
        } else {
            constraints.clear(R.id.local_companion_user_name, ConstraintSet.START)
            constraints.clear(R.id.visitor_companion_user_name, ConstraintSet.END)

            constraints.connect(R.id.local_companion_user_name, ConstraintSet.END, R.id.companions_main_container, ConstraintSet.END)
            constraints.connect(R.id.visitor_companion_user_name, ConstraintSet.START, R.id.companions_main_container, ConstraintSet.START)
        }

        val (localCompanion, visitorCompanion) = if (isLocalMatch) {
            Pair(match.localCompanion, match.visitorCompanion)
        } else {
            Pair(match.visitorCompanion, match.localCompanion)
        }
        constraints.applyTo(companionsMainContainer)

        localCompanionImage.load(localCompanion?.userImage, R.drawable.ic_incognito, true)
        localCompanionUserName.text = localCompanion?.userName
        visitorCompanionImage.load(visitorCompanion?.userImage, R.drawable.ic_incognito, true)
        visitorCompanionUserName.text = visitorCompanion?.userName
    }
}

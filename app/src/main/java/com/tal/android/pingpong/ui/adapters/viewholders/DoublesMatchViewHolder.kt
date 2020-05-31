package com.tal.android.pingpong.ui.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.utils.load

class DoublesMatchViewHolder(itemView: View) : BaseMatchViewHolder(itemView) {
    val localCompanionUserImage: ImageView = itemView.findViewById(R.id.local_companion_image)
    private val localCompanionUserName: TextView = itemView.findViewById(R.id.local_companion_user_name)
    val visitorCompanionUserImage: ImageView = itemView.findViewById(R.id.visitor_companion_image)
    private val visitorCompanionUserName: TextView = itemView.findViewById(R.id.visitor_companion_user_name)

    val localUserConfirmedImage: ImageView? = itemView.findViewById(R.id.local_user_confirmed)
    val localCompanionUserConfirmedImage: ImageView? = itemView.findViewById(R.id.local_companion_user_confirmed)
    val visitorUserConfirmedImage: ImageView? = itemView.findViewById(R.id.visitor_user_confirmed)
    val visitorCompanionUserConfirmedImage: ImageView? = itemView.findViewById(R.id.visitor_companion_user_confirmed)

    override fun bindBasicMatchData(match: MatchRecord, myEmail: String?) {
        super.bindBasicMatchData(match, myEmail)

        visitorUserImage.alpha = getUserImageAlpha(match.visitorUserConfirmed)

        localCompanionUserName.text = match.localCompanion?.userName
        localCompanionUserImage.load(
            match.localCompanion?.userImage,
            R.drawable.ic_incognito,
            true,
            getUserImageAlpha(match.localCompanionUserConfirmed)
        )

        visitorCompanionUserName.text = match.visitorCompanion?.userName
        visitorCompanionUserImage.load(
            match.visitorCompanion?.userImage,
            R.drawable.ic_incognito,
            true,
            getUserImageAlpha(match.visitorCompanionUserConfirmed)
        )

        localUserConfirmedImage?.visibility = View.VISIBLE
        localCompanionUserConfirmedImage?.visibility = getConfirmedVisibility(match.localCompanionUserConfirmed)
        visitorUserConfirmedImage?.visibility = getConfirmedVisibility(match.visitorUserConfirmed)
        visitorCompanionUserConfirmedImage?.visibility = getConfirmedVisibility(match.visitorCompanionUserConfirmed)
    }

    fun getConfirmedVisibility(confirmed: Boolean?) = if (confirmed == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

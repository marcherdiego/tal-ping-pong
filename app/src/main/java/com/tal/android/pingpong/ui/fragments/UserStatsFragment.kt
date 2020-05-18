package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.tal.android.pingpong.utils.load

class UserStatsFragment : Fragment() {

    private lateinit var userType: TextView
    private lateinit var userConfirmed: TextView

    private lateinit var userName: TextView
    private lateinit var userImage: ImageView
    private lateinit var userEmail: TextView
    private lateinit var userMatchesWon: TextView
    private lateinit var userMatchesLost: TextView
    private lateinit var userWinRate: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.user_stats_page, container, false)
        userType = view.findViewById(R.id.user_type)
        userConfirmed = view.findViewById(R.id.user_confirmed)
        userName = view.findViewById(R.id.user_name)
        userImage = view.findViewById(R.id.user_image)
        userEmail = view.findViewById(R.id.user_email)
        userMatchesWon = view.findViewById(R.id.user_matches_won)
        userMatchesLost = view.findViewById(R.id.user_matches_lost)
        userWinRate = view.findViewById(R.id.user_matches_win_rate)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = (arguments?.getSerializable(USER) as? User) ?: return
        val match = (arguments?.getSerializable(MATCH) as? MatchRecord) ?: return
        val context = context ?: return
        val preferences = SharedPreferencesUtils(context)
        val isMyUser = user.userId == preferences.getUser()?.userId

        userType.setText(
            if (match.isLocalUser(user)) {
                R.string.local
            } else {
                R.string.visitor
            }
        )
        userConfirmed.setText(
            if (match.userConfirmedDoublesMatch(user.userId)) {
                R.string.confirmed
            } else {
                R.string.unconfirmed
            }
        )

        userEmail.text = user.userEmail
        userImage.load(user.userImage, R.drawable.ic_incognito, true)

        if (isMyUser) {
            userName.text = context.getString(R.string.your_stats)
            userMatchesWon.text = context.getString(R.string.your_matches_won, user.matchesWon)
            userMatchesLost.text = context.getString(R.string.your_matches_lost, user.matchesLost)
            userWinRate.text = context.getString(R.string.your_matches_win_rate, user.matchesRatio)
        } else {
            userName.text = context.getString(R.string.x_stats, user.userName)
            userMatchesWon.text = context.getString(R.string.x_matches_won, user.firstName(), user.matchesWon)
            userMatchesLost.text = context.getString(R.string.x_matches_lost, user.firstName(), user.matchesLost)
            userWinRate.text = context.getString(R.string.x_matches_win_rate, user.firstName(), user.matchesRatio)
        }
    }

    companion object {
        const val USER = "user"
        const val MATCH = "match"
    }
}

package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.tal.android.pingpong.utils.load

class UserStatsFragment : Fragment() {

    private lateinit var localUserName: TextView
    private lateinit var userImage: ImageView
    private lateinit var localUserEmail: TextView
    private lateinit var localUserMatchesWon: TextView
    private lateinit var localUserMatchesLost: TextView
    private lateinit var localUserWinRate: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.user_stats_page, container, false)
        localUserName = view.findViewById(R.id.user_name)
        userImage = view.findViewById(R.id.user_image)
        localUserEmail = view.findViewById(R.id.user_email)
        localUserMatchesWon = view.findViewById(R.id.user_matches_won)
        localUserMatchesLost = view.findViewById(R.id.user_matches_lost)
        localUserWinRate = view.findViewById(R.id.user_matches_win_rate)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = (arguments?.getSerializable(USER) as? User) ?: return
        val context = context ?: return
        val preferences = SharedPreferencesUtils(context)
        val isMyUser = user.userId == preferences.getUser()?.userId

        localUserEmail.text = user.userEmail
        userImage.load(user.userImage, R.drawable.ic_incognito, true)

        if (isMyUser) {
            localUserName.text = context.getString(R.string.your_stats)
            localUserMatchesWon.text = context.getString(R.string.your_matches_won, user.matchesWon)
            localUserMatchesLost.text = context.getString(R.string.your_matches_lost, user.matchesLost)
            localUserWinRate.text = context.getString(R.string.your_matches_win_rate, user.matchesRatio)
        } else {
            localUserName.text = context.getString(R.string.x_stats, user.userName)
            localUserMatchesWon.text = context.getString(R.string.x_matches_won, user.firstName(), user.matchesWon)
            localUserMatchesLost.text = context.getString(R.string.x_matches_lost, user.firstName(), user.matchesLost)
            localUserWinRate.text = context.getString(R.string.x_matches_win_rate, user.firstName(), user.matchesRatio)
        }
    }

    companion object {
        const val USER = "user"
    }
}

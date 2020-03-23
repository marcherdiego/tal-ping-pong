package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.PastMatchesAdapter
import com.tal.android.pingpong.utils.GlideUtils

class UserProfileView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val logoutButton: FloatingActionButton? = fragment.view?.findViewById(R.id.logout_button)

    private val userImage: ImageView? = fragment.view?.findViewById(R.id.user_image)
    private val collapsingToolbar: CollapsingToolbarLayout? = fragment.view?.findViewById(R.id.collapsing_toolbar)

    private val email: TextView? = fragment.view?.findViewById(R.id.email)
    private val matchesWon: TextView? = fragment.view?.findViewById(R.id.matches_won)
    private val matchesLost: TextView? = fragment.view?.findViewById(R.id.matches_lost)
    private val matchWinRate: TextView? = fragment.view?.findViewById(R.id.matches_win_rate)
    private val lastTenMatches: RecyclerView? = fragment.view?.findViewById(R.id.last_ten_matches)
    private val matchesLoaderProgressBar: ProgressBar? = fragment.view?.findViewById(R.id.matches_loader_progress_bar)

    init {
        logoutButton?.setOnClickListener {
            bus.post(LogoutButtonClickedEvent())
        }
    }

    fun hideLogoutButton() {
        logoutButton?.hide()
    }

    fun loadUserBasicInfo(imageUrl: String?, name: String?, email: String?) {
        GlideUtils.loadImage(imageUrl, userImage, R.drawable.ic_incognito)
        collapsingToolbar?.title = name
        this.email?.text = email
    }

    fun loadUserMatchesInfo(matchesWon: String?, matchesLost: String?, matchWinRate: String?) {
        this.matchesWon?.text = matchesWon
        this.matchesLost?.text = matchesLost
        this.matchWinRate?.text = matchWinRate
    }

    fun setPastMatchesAdapter(adapter: PastMatchesAdapter) {
        matchesLoaderProgressBar?.visibility = View.GONE
        lastTenMatches?.adapter = adapter
    }

    class LogoutButtonClickedEvent
}

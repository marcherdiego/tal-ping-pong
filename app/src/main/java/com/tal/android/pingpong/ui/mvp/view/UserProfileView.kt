package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.EmptyAdapter
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.load

class UserProfileView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val logoutButton: FloatingActionButton? = fragment.activity?.findViewById(R.id.logout_button)

    private val userImage: ImageView? = fragment.activity?.findViewById(R.id.user_image)
    private val collapsingToolbar: CollapsingToolbarLayout? = fragment.activity?.findViewById(R.id.collapsing_toolbar)
    private val toolbar: Toolbar? = fragment.activity?.findViewById(R.id.toolbar)

    private val email: TextView? = fragment.view?.findViewById(R.id.email)
    private val matchesWon: TextView? = fragment.view?.findViewById(R.id.matches_won)
    private val matchesLost: TextView? = fragment.view?.findViewById(R.id.matches_lost)
    private val matchWinRate: TextView? = fragment.view?.findViewById(R.id.matches_win_rate)

    private val successRateBar: DifficultyBar? = fragment.view?.findViewById(R.id.success_rate_bar)

    private val emptyListLayout: View? = fragment.view?.findViewById(R.id.empty_list_layout)
    private val emptyListEmoji: ImageView? = emptyListLayout?.findViewById(R.id.emoji)
    private val emptyListMessage: TextView? = emptyListLayout?.findViewById(R.id.message)
    private val lastTenMatches: RecyclerView? = fragment.view?.findViewById(R.id.last_ten_matches)
    private val matchesLoaderProgressBar: ProgressBar? = fragment.view?.findViewById(R.id.matches_loader_progress_bar)

    init {
        logoutButton?.setOnClickListener {
            bus.post(LogoutButtonClickedEvent())
        }
        lastTenMatches?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )
    }

    fun showLogoutButton() {
        logoutButton?.show()
    }

    fun hideLogoutButton() {
        logoutButton?.hide()
    }

    fun loadUserBasicInfo(imageUrl: String?, name: String?, email: String?) {
        userImage.load(imageUrl, R.drawable.ic_incognito)
        collapsingToolbar?.title = name
        toolbar?.title = name
        this.email?.text = email
    }

    fun setupSuccessRateBar(matchesRatioValue: Float, successBarTitle: String?) {
        successRateBar?.setupForMe(matchesRatioValue)
        successRateBar?.setTitle(successBarTitle)
    }

    fun loadUserMatchesInfo(matchesWon: String?, matchesLost: String?, matchWinRate: String?) {
        this.matchesWon?.text = matchesWon
        this.matchesLost?.text = matchesLost
        this.matchWinRate?.text = matchWinRate
    }

    fun showMatchesLoaderProgressBar() {
        matchesLoaderProgressBar?.visibility = View.VISIBLE
    }

    fun hideMatchesLoaderProgressBar() {
        matchesLoaderProgressBar?.visibility = View.GONE
    }

    fun showNetworkErrorMessage() {
        emptyListLayout?.visibility = View.VISIBLE
        emptyListEmoji?.setImageResource(R.drawable.ic_confused)
        emptyListMessage?.setText(R.string.network_error_message)
    }

    fun setPastMatchesAdapter(adapter: RecyclerView.Adapter<*>) {
        if (adapter.itemCount == 0) {
            emptyListLayout?.visibility = View.VISIBLE
            emptyListEmoji?.setImageResource(R.drawable.ic_sad)
            emptyListMessage?.setText(R.string.no_matches_yet)
            lastTenMatches?.adapter = EmptyAdapter()
        } else {
            emptyListLayout?.visibility = View.GONE
            lastTenMatches?.adapter = adapter
        }
    }

    class LogoutButtonClickedEvent
}

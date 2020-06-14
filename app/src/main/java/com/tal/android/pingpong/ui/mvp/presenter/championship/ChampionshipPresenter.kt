package com.tal.android.pingpong.ui.mvp.presenter.championship

import android.view.MenuItem
import androidx.core.os.bundleOf
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.tabs.TabsAdapter
import com.tal.android.pingpong.ui.fragments.championship.ChampionshipUsersListFragment
import com.tal.android.pingpong.ui.fragments.championship.ChampionshipMatchesFragment
import com.tal.android.pingpong.ui.fragments.championship.ChampionshipStandingsFragment

import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipModel
import com.tal.android.pingpong.ui.mvp.view.ChampionshipView
import com.tal.android.pingpong.utils.DateUtils
import java.util.*

class ChampionshipPresenter(view: ChampionshipView, model: ChampionshipModel) :
    BaseActivityPresenter<ChampionshipView, ChampionshipModel>(view, model) {
    init {
        with(model.championship) {
            view.withFragmentManager {
                val matchesFragment = ChampionshipMatchesFragment().apply {
                    arguments = bundleOf(ChampionshipMatchesFragment.CHAMPIONSHIP_ID to championshipId)
                }
                val usersFragment = ChampionshipUsersListFragment().apply {
                    arguments = bundleOf(ChampionshipUsersListFragment.CHAMPIONSHIP_ID to championshipId)
                }
                val standingsFragment = ChampionshipStandingsFragment().apply {
                    arguments = bundleOf(ChampionshipStandingsFragment.CHAMPIONSHIP_ID to championshipId)
                }
                val tabsFragment = listOf(
                    matchesFragment,
                    usersFragment,
                    standingsFragment
                )
                val titles = listOf(
                    ChampionshipMatchesFragment.TITLE,
                    ChampionshipUsersListFragment.TITLE,
                    ChampionshipStandingsFragment.TITLE
                )
                view.setTabsAdapter(TabsAdapter(this, tabsFragment, titles))
            }
            view.setTitle(championshipName)
            view.setCreator(creator?.userName)
            val championshipStartDate = DateUtils.parseDate(championshipDate)
            val formattedDate = DateUtils.formatDate(championshipDate)
            view.setDate(
                view.activity?.getString(
                    if (championshipStartDate?.before(Date()) == true) {
                        R.string.started_on_x
                    } else {
                        R.string.starts_on_x
                    },
                    formattedDate
                )
            )

            view.setMembersCount(usersCount ?: 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

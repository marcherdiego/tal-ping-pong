package com.tal.android.pingpong.ui.mvp.presenter.championship

import android.view.MenuItem
import androidx.core.os.bundleOf
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.tabs.TabsAdapter
import com.tal.android.pingpong.ui.fragments.championship.ReadOnlyUsersListFragment
import com.tal.android.pingpong.ui.fragments.championship.ReadOnlyMatchesFragment

import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipModel
import com.tal.android.pingpong.ui.mvp.view.ChampionshipView
import com.tal.android.pingpong.utils.DateUtils
import java.util.*

class ChampionshipPresenter(view: ChampionshipView, model: ChampionshipModel) :
    BaseActivityPresenter<ChampionshipView, ChampionshipModel>(view, model) {
    init {
        with(model.championship) {
            view.withFragmentManager {
                val matchesFragment = ReadOnlyMatchesFragment().apply {
                    arguments = bundleOf(ReadOnlyMatchesFragment.CHAMPIONSHIP_ID to championshipId)
                }
                val usersFragment = ReadOnlyUsersListFragment().apply {
                    arguments = bundleOf(ReadOnlyUsersListFragment.CHAMPIONSHIP_ID to championshipId)
                }
                val tabsFragment = listOf(matchesFragment, usersFragment)
                val titles = listOf(ReadOnlyMatchesFragment.TITLE, ReadOnlyUsersListFragment.TITLE)
                view.setTabsAdapter(TabsAdapter(this, tabsFragment, titles))
            }
            view.setTitle(championshipName)
            view.setCreator(creator?.userEmail)
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

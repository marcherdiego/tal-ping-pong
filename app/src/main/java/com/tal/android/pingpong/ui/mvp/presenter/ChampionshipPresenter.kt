package com.tal.android.pingpong.ui.mvp.presenter

import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.ui.adapters.TabsAdapter
import com.tal.android.pingpong.ui.fragments.MatchesListFragment
import com.tal.android.pingpong.ui.fragments.ReadOnlyUsersListFragment
import com.tal.android.pingpong.ui.fragments.UsersListFragment
import com.tal.android.pingpong.ui.fragments.matcheslist.ReadOnlyMatchesFragment

import com.tal.android.pingpong.ui.mvp.model.ChampionshipModel
import com.tal.android.pingpong.ui.mvp.view.ChampionshipView

class ChampionshipPresenter(view: ChampionshipView, model: ChampionshipModel) :
    BaseActivityPresenter<ChampionshipView, ChampionshipModel>(view, model) {
    init {
        view.withFragmentManager {
            val tabsFragment = listOf(ReadOnlyMatchesFragment(), ReadOnlyUsersListFragment())
            val titles = listOf(ReadOnlyMatchesFragment.TITLE, ReadOnlyUsersListFragment.TITLE)
            view.setTabsAdapter(TabsAdapter(this, tabsFragment, titles))
        }
    }
}

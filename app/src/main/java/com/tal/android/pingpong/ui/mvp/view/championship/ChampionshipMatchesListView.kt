package com.tal.android.pingpong.ui.mvp.view.championship

import androidx.fragment.app.Fragment
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView

import com.tal.android.pingpong.R

open class ChampionshipMatchesListView(fragment: Fragment) : BaseMatchesListView(fragment) {
    init {
        onClick(R.id.new_championship_match_button, NewChampionshipMatchButtonClickedEvent())
    }

    class NewChampionshipMatchButtonClickedEvent
}

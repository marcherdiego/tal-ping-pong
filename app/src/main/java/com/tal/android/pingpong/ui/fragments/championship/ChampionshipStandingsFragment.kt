package com.tal.android.pingpong.ui.fragments.championship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipStandingsModel
import com.tal.android.pingpong.ui.mvp.presenter.championship.ChampionshipStandingsPresenter
import com.tal.android.pingpong.ui.mvp.view.championship.ChampionshipStandingsView

class ChampionshipStandingsFragment : BaseFragment<ChampionshipStandingsPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.standings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val championshipId = arguments?.getInt(ChampionshipUsersListFragment.CHAMPIONSHIP_ID)
        presenter = ChampionshipStandingsPresenter(
            ChampionshipStandingsView(this),
            ChampionshipStandingsModel(championshipId ?: return)
        )
    }

    companion object {
        const val CHAMPIONSHIP_ID = "championship_id"
        const val TITLE = "Standings"
    }
}

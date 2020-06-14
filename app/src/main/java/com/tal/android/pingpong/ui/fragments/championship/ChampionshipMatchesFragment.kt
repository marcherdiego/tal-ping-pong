package com.tal.android.pingpong.ui.fragments.championship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.fragments.matcheslist.BaseMatchesList
import com.tal.android.pingpong.ui.mvp.model.championship.ChampionshipMatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.championship.ChampionshipMatchesPresenter
import com.tal.android.pingpong.ui.mvp.view.championship.ChampionshipMatchesListView
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.tal.android.pingpong.utils.multiLet

class ChampionshipMatchesFragment : BaseMatchesList<ChampionshipMatchesPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.matches_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val championshipId = arguments?.getInt(ChampionshipUsersListFragment.CHAMPIONSHIP_ID)
        multiLet(championshipId, context) { championshipId, context ->
            presenter = ChampionshipMatchesPresenter(
                ChampionshipMatchesListView(this),
                ChampionshipMatchesModel(
                    SharedPreferencesUtils(context),
                    championshipId
                ),
                Bus.newInstance
            )
        }
    }

    companion object {
        const val CHAMPIONSHIP_ID = "championship_id"
        const val TITLE = "Matches"
    }
}
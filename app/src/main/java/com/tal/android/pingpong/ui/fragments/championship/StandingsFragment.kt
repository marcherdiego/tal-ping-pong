package com.tal.android.pingpong.ui.fragments.championship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.championship.StandingsModel
import com.tal.android.pingpong.ui.mvp.presenter.championship.StandingsPresenter
import com.tal.android.pingpong.ui.mvp.view.championship.StandingsView

class StandingsFragment : BaseFragment<StandingsPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.standings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val championshipId = arguments?.getInt(ReadOnlyUsersListFragment.CHAMPIONSHIP_ID)
        presenter = StandingsPresenter(
            StandingsView(this),
            StandingsModel(championshipId ?: return)
        )
    }

    companion object {
        const val CHAMPIONSHIP_ID = "championship_id"
        const val TITLE = "Standings"
    }
}

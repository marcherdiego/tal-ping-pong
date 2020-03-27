package com.tal.android.pingpong.ui.fragments.matcheslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.matcheslist.UnconfirmedMatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.matcheslist.UnconfirmedMatchesPresenter
import com.tal.android.pingpong.ui.mvp.view.matcheslist.BaseMatchesListView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UnconfirmedMatchesFragment : BaseFragment<UnconfirmedMatchesPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.unconfirmed_matches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let {
            val sharedPreferences = SharedPreferencesUtils(it)
            presenter = UnconfirmedMatchesPresenter(
                BaseMatchesListView(this),
                UnconfirmedMatchesModel(sharedPreferences),
                Bus.newInstance
            )
        }
    }
}

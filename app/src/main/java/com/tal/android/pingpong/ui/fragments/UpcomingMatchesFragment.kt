package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.UpcomingMatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.UpcomingMatchesPresenter
import com.tal.android.pingpong.ui.mvp.view.UpcomingMatchesView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UpcomingMatchesFragment : BaseFragment<UpcomingMatchesPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.matches_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let {
            val sharedPreferences = SharedPreferencesUtils(it)
            presenter = UpcomingMatchesPresenter(
                UpcomingMatchesView(this),
                UpcomingMatchesModel(sharedPreferences),
                Bus.newInstance
            )
        }
    }
}

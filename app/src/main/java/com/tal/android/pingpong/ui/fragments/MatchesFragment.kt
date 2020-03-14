package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.MatchesModel
import com.tal.android.pingpong.ui.mvp.presenter.MatchesPresenter
import com.tal.android.pingpong.ui.mvp.view.MatchesView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class MatchesFragment : BaseFragment<MatchesPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.matches_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        context?.let {
            val sharedPreferences = SharedPreferencesUtils(it)
            presenter = MatchesPresenter(
                MatchesView(this),
                MatchesModel(sharedPreferences)
            )
        }
    }
}

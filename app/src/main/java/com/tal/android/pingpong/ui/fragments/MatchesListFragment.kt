package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.MatchesListModel
import com.tal.android.pingpong.ui.mvp.presenter.MatchesListPresenter
import com.tal.android.pingpong.ui.mvp.view.MatchesListView

class MatchesListFragment : BaseFragment<MatchesListPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.matches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = MatchesListPresenter(
                MatchesListView(this),
                MatchesListModel()
        )
    }
}

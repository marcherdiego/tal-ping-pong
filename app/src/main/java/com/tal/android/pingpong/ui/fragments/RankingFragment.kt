package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.RankingModel
import com.tal.android.pingpong.ui.mvp.presenter.RankingPresenter
import com.tal.android.pingpong.ui.mvp.view.RankingView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class RankingFragment : BaseFragment<RankingPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ranking_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            presenter = RankingPresenter(
                RankingView(this),
                RankingModel(SharedPreferencesUtils(it))
            )
        }
    }
}

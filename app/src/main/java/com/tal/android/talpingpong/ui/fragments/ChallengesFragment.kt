package com.tal.android.talpingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.ui.mvp.model.ChallengesModel
import com.tal.android.talpingpong.ui.mvp.presenter.ChallengesPresenter
import com.tal.android.talpingpong.ui.mvp.view.ChallengesView

class ChallengesFragment : BaseFragment<ChallengesPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.challenges_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = ChallengesPresenter(
            ChallengesView(this),
            ChallengesModel()
        )
    }
}

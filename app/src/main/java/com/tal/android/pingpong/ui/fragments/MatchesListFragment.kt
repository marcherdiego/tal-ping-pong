package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.notifications.Constants
import com.tal.android.pingpong.ui.activities.MainActivity
import com.tal.android.pingpong.ui.mvp.model.MatchesListModel
import com.tal.android.pingpong.ui.mvp.presenter.MatchesListPresenter
import com.tal.android.pingpong.ui.mvp.view.MatchesListView

class MatchesListFragment : BaseFragment<MatchesListPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.matches_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var match: MatchRecord? = null
        var selectedTab: String = MatchesListModel.Companion.TabsState.UPCOMING
        var actionType: String = Constants.NONE
        arguments?.let {
            match = it.getSerializable(EXTRA_MATCH) as? MatchRecord
            selectedTab = it.getString(EXTRA_TAB) ?: MatchesListModel.Companion.TabsState.UPCOMING
            actionType = it.getString(ACTION_TYPE) ?: Constants.NONE
        }
        presenter = MatchesListPresenter(
            MatchesListView(this),
            MatchesListModel(match, selectedTab, actionType)
        )
    }

    companion object {
        const val EXTRA_MATCH = "match"
        const val EXTRA_TAB = "tab"
        const val ACTION_TYPE = "action"
    }
}

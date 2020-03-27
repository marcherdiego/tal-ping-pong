package com.tal.android.pingpong.ui.mvp.presenter

import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.MatchesTabsAdapter
import com.tal.android.pingpong.ui.fragments.matcheslist.PastMatchesFragment
import com.tal.android.pingpong.ui.fragments.matcheslist.UnconfirmedMatchesFragment
import com.tal.android.pingpong.ui.fragments.matcheslist.UpcomingMatchesFragment
import com.tal.android.pingpong.ui.mvp.view.MatchesListView
import com.tal.android.pingpong.ui.mvp.model.MatchesListModel

class MatchesListPresenter(view: MatchesListView, model: MatchesListModel) :
    BaseFragmentPresenter<MatchesListView, MatchesListModel>(view, model) {

    init {
        view.withFragmentManager {
            view.context?.let {
                val fragments = listOf<Fragment>(
                    UpcomingMatchesFragment(),
                    UnconfirmedMatchesFragment(),
                    PastMatchesFragment()
                )
                val titles = listOf(
                    it.getString(R.string.upcoming_matches),
                    it.getString(R.string.unconfirmed_matches),
                    it.getString(R.string.past_matches)
                )
                view.setTabsAdapter(MatchesTabsAdapter(this, fragments, titles))
            }
        }
    }
}

package com.tal.android.pingpong.ui.fragments.matcheslist

import com.nerdscorner.mvplib.events.bus.Bus
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.presenter.matcheslist.BaseMatchesListPresenter

open class BaseMatchesList<P : BaseMatchesListPresenter<*, *>> : BaseFragment<P>() {
    override fun onResume() {
        super.onResume()
        Bus.registerDefault(presenter)
    }

    override fun onPause() {
        Bus.unregisterDefault(presenter)
        super.onPause()
    }
}
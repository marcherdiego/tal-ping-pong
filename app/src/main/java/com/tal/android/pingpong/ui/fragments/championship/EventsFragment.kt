package com.tal.android.pingpong.ui.fragments.championship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tal.android.pingpong.R

import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.ui.mvp.model.EventsModel
import com.tal.android.pingpong.ui.mvp.presenter.EventsPresenter
import com.tal.android.pingpong.ui.mvp.view.EventsView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class EventsFragment : BaseFragment<EventsPresenter>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.events_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            val sharedPreferences = SharedPreferencesUtils(it)
            presenter = EventsPresenter(
                EventsView(this),
                EventsModel(sharedPreferences)
            )
        }
    }
}

package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import androidx.fragment.app.Fragment

import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R

class ProfileView(fragment: Fragment) : BaseFragmentView(fragment) {
    init {
        fragment.view?.findViewById<View>(R.id.logout_button)?.setOnClickListener {
            bus.post(LogoutButtonClickedEvent())
        }
    }

    class LogoutButtonClickedEvent
}

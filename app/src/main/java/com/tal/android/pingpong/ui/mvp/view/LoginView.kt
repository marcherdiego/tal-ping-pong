package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.tal.android.pingpong.R

class LoginView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    init {
        activity.findViewById<View>(R.id.sign_in_button).setOnClickListener {
            bus.post(SignInButtonClickedEvent())
        }
    }

    class SignInButtonClickedEvent
}

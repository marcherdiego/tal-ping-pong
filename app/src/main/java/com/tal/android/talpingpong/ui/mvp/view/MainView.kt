package com.tal.android.talpingpong.ui.mvp.view

import android.widget.TextView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.tal.android.talpingpong.R

class MainView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val userName: TextView = activity.findViewById(R.id.user_name)

    fun loadUserData(userName: String?, userEmail: String?) {
        this.userName.text = "$userName ($userEmail)"
    }
}

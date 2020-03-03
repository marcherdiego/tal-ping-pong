package com.tal.android.pingpong

import android.app.Application
import com.tal.android.pingpong.data.ChallengesManager
import com.tal.android.pingpong.data.UsersManager

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        UsersManager.init()
        ChallengesManager.init()
    }
}
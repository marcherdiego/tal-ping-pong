package com.tal.android.talpingpong.ui.mvp.model

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.nerdscorner.mvplib.events.model.BaseEventsModel

class MainModel(context: Context) : BaseEventsModel() {
    val currentUser = GoogleSignIn.getLastSignedInAccount(context)
}

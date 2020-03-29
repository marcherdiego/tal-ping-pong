package com.tal.android.pingpong.ui.mvp.model

import android.util.Log
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import retrofit2.Call

open class BaseModel : BaseEventsModel() {
    val retrofitCalls = mutableListOf<Call<*>>()

    override fun onPause() {
        while (retrofitCalls.size > 0) {
            val call = retrofitCalls.removeAt(0)
            Log.w("Cancelling call", call.toString())
            call.cancel()
        }
        super.onPause()
    }
}
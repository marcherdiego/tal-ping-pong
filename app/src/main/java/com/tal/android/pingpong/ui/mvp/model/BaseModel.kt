package com.tal.android.pingpong.ui.mvp.model

import com.nerdscorner.mvplib.events.model.BaseEventsModel
import retrofit2.Call

open class BaseModel : BaseEventsModel() {
    private val retrofitCalls = mutableMapOf<String, Call<*>>()

    fun addCall(call: Call<*>) {
        with(call) {
            val key = getCallKey(call)

            // Cancel previous call, if exists
            removeCall(this)?.cancel()

            // Add new call
            retrofitCalls[key] = this
        }
    }

    fun removeCall(call: Call<*>): Call<*>? {
        val key = getCallKey(call)
        return retrofitCalls.remove(key)
    }

    private fun getCallKey(call: Call<*>) = with(call) {
        "${request().method} ${request().url}"
    }

    override fun onPause() {
        retrofitCalls.entries.forEach {
            it.value.cancel()
        }
        retrofitCalls.clear()
        super.onPause()
    }
}
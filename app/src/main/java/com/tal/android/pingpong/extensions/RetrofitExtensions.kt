package com.tal.android.pingpong.extensions

import android.util.Log
import com.nerdscorner.mvplib.events.model.BaseEventsModel
import com.tal.android.pingpong.exceptions.NetworkException
import com.tal.android.pingpong.ui.mvp.model.BaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val X_ERROR_CODE = "X-ERROR-CODE"
private const val X_ERROR_MESSAGE = "X-ERROR-MESSAGE"

private const val TAG = "NETWORK CALL"

fun <T> Call<T>.enqueue(
    success: (T?) -> Unit = {},
    successCacheCheck: (Boolean, T?) -> Unit = { _, _ -> },
    fail: (NetworkException) -> Unit = {},
    model: BaseModel? = null
): Call<T> {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            model?.removeCall(this@enqueue)
            if (response.isSuccessful) {
                val body = response.body()
                success(body)
                successCacheCheck(response.isFromCache(), body)
                Log.d(
                    TAG,
                    "${call.request().method}: " +
                            "${call.request().url} -> Response from " +
                            if (response.isFromCache()) {
                                "Cache"
                            } else {
                                "Network"
                            }
                )
            } else {
                fail(
                    NetworkException(response.message())
                        .setStatusCode(response.code())
                        .setErrorBody(response.errorBody()?.string())
                        .setErrorCode(response.headers()[X_ERROR_CODE])
                        .setErrorMessage(response.headers()[X_ERROR_MESSAGE])
                        .setResponse(response)
                )
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) {
                return
            }
            fail(
                NetworkException(t.message)
                    .setThrowable(t)
            )
        }
    })
    model?.addCall(this)
    return this
}

fun <T> Call<T>.enqueueResponseNotNull(
    success: (T) -> Unit = {},
    successCacheCheck: (Boolean, T) -> Unit = { _, _ -> },
    fail: (NetworkException) -> Unit = {},
    model: BaseModel? = null
): Call<T> {
    return enqueue(
        success = { body ->
            body?.let {
                success(it)
            }
        },
        successCacheCheck = { fromCache, body ->
            body?.let {
                successCacheCheck(fromCache, it)
            }
        },
        fail = fail,
        model = model
    )
}

fun <T> Call<T>.fireAndForget(model: BaseModel? = null): Call<T> {
    return enqueue(
        success = {},
        fail = {},
        model = model
    )
}

fun Response<*>.isFromCache() = raw().networkResponse == null

package com.tal.android.pingpong.exceptions

import retrofit2.Response

class NetworkException(message: String?) : Exception(message) {
    var statusCode: Int? = null
    var errorCode: String? = null
    var errorMessage: String? = null
    var errorBody: String? = null
    var throwable: Throwable? = null
    var response: Response<*>? = null

    @JvmName("statusCodeSet")
    fun setStatusCode(statusCode: Int?): NetworkException {
        this.statusCode = statusCode
        return this
    }

    @JvmName("errorCodeSet")
    fun setErrorCode(errorCode: String?): NetworkException {
        this.errorCode = errorCode
        return this
    }

    @JvmName("errorMessageSet")
    fun setErrorMessage(errorMessage: String?): NetworkException {
        this.errorMessage = errorMessage
        return this
    }

    @JvmName("errorBodySet")
    fun setErrorBody(errorBody: String?): NetworkException {
        this.errorBody = errorBody
        return this
    }

    @JvmName("throwableSet")
    fun setThrowable(throwable: Throwable?): NetworkException {
        this.throwable = throwable
        return this
    }

    @JvmName("responseSet")
    fun setResponse(response: Response<*>?): NetworkException {
        this.response = response
        return this
    }
}

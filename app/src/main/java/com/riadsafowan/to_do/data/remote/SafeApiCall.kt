package com.riadsafowan.to_do.data.remote

import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Response<T> {
        return try {
            Response.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    Response.Failure(false, throwable.code(), throwable.response()?.errorBody())
                }
                else -> {
                    Response.Failure(true, null, null)
                }
            }
        }
    }
}
package com.riadsafowan.to_do.data.remote

import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ApiResult<T> {
        return try {
            ApiResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    ApiResult.Failure(false, throwable.code(), throwable.response()?.errorBody())
                }
                else -> {
                    ApiResult.Failure(true, null, null)
                }
            }
        }
    }
}
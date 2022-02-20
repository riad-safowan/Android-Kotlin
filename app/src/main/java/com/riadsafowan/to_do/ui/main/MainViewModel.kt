package com.riadsafowan.to_do.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.login.LoginResponse
import com.riadsafowan.to_do.data.remote.ApiClient
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import com.riadsafowan.to_do.data.remote.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val apiRepository: ApiRepository
) : ViewModel() {

    val userDataFlow = userDataStore.getUser()

    fun logout() = viewModelScope.launch {
        userDataStore.save(UserData("", "", "", false))
    }

    fun uploadImage(image: MultipartBody.Part) = viewModelScope.launch {
        val response = apiRepository.uploadProfileImage(image)
        if (response is ApiResult.Success) {
            val name = "${response.value.data?.firstName} ${response.value.data?.lastName}"
            userDataStore.save(
                UserData(
                    name,
                    response.value.data?.email!!,
                    response.value.data.imageUrl ?: "",
                    true
                )
            )
            Log.d("TAG", "uploadImage: " + response.value.data?.imageUrl)
        } else if (response is ApiResult.Failure) {
            Log.d("TAG", "uploadImage: " + response.errorCode + " " + response.errorCode)
        }
//        val call = apiClient.uploadProfileImage(image)
//
//        call.enqueue(object : Callback<BaseResponse<LoginResponse>> {
//            override fun onResponse(
//                call: Call<BaseResponse<LoginResponse>>,
//                response: Response<BaseResponse<LoginResponse>>
//            ) {
//                Log.d("TAG", "success: ")
//            }
//
//            override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
//                Log.d("TAG", "onFailure: "+ t.message)
//            }
//
//        })

    }
}
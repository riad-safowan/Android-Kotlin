package com.riadsafowan.to_do.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

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
            userDataStore.saveImgUrl(response.value.data?.imageUrl)
        } else if (response is ApiResult.Failure) {
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
//        })

    }
}
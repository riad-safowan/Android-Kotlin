package com.riadsafowan.to_do.ui.posts.createPost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.data.model.posts.PostRequest
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val apiRepository: ApiRepository
) : ViewModel() {
    val PostResponse: MutableLiveData<PostResponse> = MutableLiveData()
    val ImageResponse: MutableLiveData<PostResponse> = MutableLiveData()

    fun createPost(text: String) = viewModelScope.launch {
        val response = apiRepository.createPost(PostRequest(text = text))

        if (response is ApiResult.Success) {
            PostResponse.postValue(response.value.data!!)
        }
    }

    fun uploadImage(image: MultipartBody.Part, postId: Int) = viewModelScope.launch {
        val response = apiRepository.uploadPostImage(image, postId)
        if (response is ApiResult.Success) {
            ImageResponse.postValue(response.value.data!!)
        } else if (response is ApiResult.Failure) {
            Log.d("TAG", "uploadImage: " + response.errorCode + " " + response.errorCode)
        }
    }

}
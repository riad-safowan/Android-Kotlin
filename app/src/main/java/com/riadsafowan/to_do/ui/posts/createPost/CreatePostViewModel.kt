package com.riadsafowan.to_do.ui.posts.createPost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.model.posts.PostRequest
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {
    val PostResponse: MutableLiveData<PostResponse> = MutableLiveData()

    fun createPost(text: String) = viewModelScope.launch {
        val response = apiRepository.createPost(PostRequest(text = text))

        if (response is ApiResult.Success) {
            PostResponse.postValue(response.value.data!!)
        }
    }

}
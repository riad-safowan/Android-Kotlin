package com.riadsafowan.to_do.ui.posts

import androidx.lifecycle.*
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    init {
        getPost()
    }

    val posts: MutableLiveData<List<PostResponse>> = MutableLiveData()

    fun getPost() = viewModelScope.launch {
        val response = apiRepository.getPosts()
        if (response is ApiResult.Success) {
            posts.postValue(response.value.data!!)
        }

    }

}




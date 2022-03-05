package com.riadsafowan.to_do.ui.posts.comments

import androidx.lifecycle.*
import com.riadsafowan.to_do.data.model.posts.PostResponse
import com.riadsafowan.to_do.data.model.posts.comment.CommentRequest
import com.riadsafowan.to_do.data.model.posts.comment.CommentResponse
import com.riadsafowan.to_do.data.remote.ApiRepository
import com.riadsafowan.to_do.data.remote.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    val comments: MutableLiveData<List<CommentResponse>> = MutableLiveData()


    fun getComments(postId: Int) = viewModelScope.launch {
        val response = apiRepository.getComments(postId)
        if (response is ApiResult.Success) {
            comments.postValue(response.value.data!!)
        }
    }

    fun createComment(postId: Int, request: CommentRequest) = viewModelScope.launch {
        val response = apiRepository.createComment(postId, request)
        if (response is ApiResult.Success) {
            comments.postValue(response.value.data!!)
        }
    }

}





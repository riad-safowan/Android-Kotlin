package com.riadsafowan.to_do.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userDataStore: UserDataStore) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData()

    val userDataFlow = userDataStore.getUser()

    fun logout() = viewModelScope.launch {
        userDataStore.save(UserData("", "", false))
    }

}
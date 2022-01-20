package com.riadsafowan.to_do.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData()
}
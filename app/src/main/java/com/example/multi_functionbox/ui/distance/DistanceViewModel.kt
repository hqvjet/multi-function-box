package com.example.multi_functionbox.ui.distance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Distance Fragment"
    }
    val text: LiveData<String> = _text
}
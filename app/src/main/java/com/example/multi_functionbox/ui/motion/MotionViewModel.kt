package com.example.multi_functionbox.ui.motion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MotionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Motion Fragment"
    }
    val text: LiveData<String> = _text
}
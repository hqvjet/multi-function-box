package com.example.multi_functionbox.ui.flame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlameViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Flame Fragment"
    }
    val text: LiveData<String> = _text
}
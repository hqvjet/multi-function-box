package com.example.multi_functionbox.ui.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Current Fragment"
    }
    val text: LiveData<String> = _text
}
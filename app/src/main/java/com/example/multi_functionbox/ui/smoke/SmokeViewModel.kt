package com.example.multi_functionbox.ui.smoke

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SmokeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Smoke Fragment"
    }
    val text: LiveData<String> = _text
}
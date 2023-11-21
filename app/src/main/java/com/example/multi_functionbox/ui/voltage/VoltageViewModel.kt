package com.example.multi_functionbox.ui.voltage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VoltageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Voltage Fragment"
    }
    val text: LiveData<String> = _text
}
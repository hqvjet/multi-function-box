package com.example.multi_functionbox.ui.temperature_humid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TemperatureHumidViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Temp-Humid Fragment"
    }
    val text: LiveData<String> = _text
}
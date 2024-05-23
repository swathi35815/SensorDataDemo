package com.example.sensordatademo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SensorViewModel : ViewModel() {

    private val _lightSensorData = MutableStateFlow(0f)
    val lightSensorData: StateFlow<Float> get() = _lightSensorData

    private val _accelerometerData = MutableStateFlow(0f)
    val accelerometerData: StateFlow<Float> get() = _accelerometerData

    fun updateLightSensorData(value: Float) {
        viewModelScope.launch {
            _lightSensorData.value = value
        }
    }

    fun updateAccelerometerData(value: Float) {
        viewModelScope.launch {
            _accelerometerData.value = value
        }
    }
}
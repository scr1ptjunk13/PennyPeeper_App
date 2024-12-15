package com.example.pp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PennyPeeperViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _canTakeScreenshot = MutableStateFlow(false)
    val canTakeScreenshot: StateFlow<Boolean> = _canTakeScreenshot.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        if (_count.value > 0) {
            _count.value--
        }
    }

    fun onPermissionsGranted() {
        _canTakeScreenshot.value = true
    }
}


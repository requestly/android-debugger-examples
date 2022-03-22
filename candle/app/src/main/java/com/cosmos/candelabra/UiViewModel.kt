package com.cosmos.candelabra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cosmos.candelabra.util.extension.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UiViewModel @Inject constructor() : ViewModel() {

    private val _navigationVisibility = MutableLiveData<Boolean>()
    val navigationVisibility: LiveData<Boolean> get() = _navigationVisibility

    fun setNavigationVisibility(visible: Boolean) {
        _navigationVisibility.updateValue(visible)
    }
}

package com.example.templateproject.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.templateproject.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel(){
    private val _data = MutableLiveData("")
    val data : LiveData<String>
        get() = _data

    fun getDataFromApi(){
        viewModelScope.launch {
            delay(2000)
            _data.value = "Response"
        }
    }
}
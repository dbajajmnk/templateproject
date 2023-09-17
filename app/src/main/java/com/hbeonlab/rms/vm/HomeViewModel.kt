package com.hbeonlab.rms.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hbeonlab.rms.base.BaseViewModel
import com.hbeonlab.rms.data.RmsRepository
import com.hbeonlab.rms.data.models.ApiResponse
import com.hbeonlab.rms.data.models.RmsData
import com.hbeonlab.rms.network.RetrofitClient
import com.hbeonlab.rms.ui.MessageItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(val rmsRepository: RmsRepository) : BaseViewModel(){
    private val _data = MutableLiveData<String>()
    val data : LiveData<String>
        get() = _data

    val chatMessages = MutableLiveData<MutableList<MessageItem>>(mutableListOf())

    fun sendDataToApi(){
        viewModelScope.launch {
            //TODO sent dummy data in api, need to be replaced with actual one
            val apiResponseCall = RetrofitClient.rmsApi.sendRmsData("868984040220622,01,01,15,118,009,145,25,1100,030000,21,2022-10-31 19:19:11,1,2,1,20,12.42515705,17.54472146,0,1,12,1023")
            apiResponseCall.enqueue(object : Callback<ApiResponse?> {
                override fun onResponse(
                    call: Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    _data.value = response.body()?.message
                }

                override fun onFailure(call: Call<ApiResponse?>, t: Throwable) {
                    val result: String = t.toString()
                    _data.value = result
                }
            })
        }
    }

    fun addDataToDb(rmsString: String){
        val rmsData = RmsData.RmsConverter.convert(rmsString)
        viewModelScope.launch {
            rmsRepository.rmsDao.insertRmsData(rmsData)
        }
    }

    fun getAllRmsData() = rmsRepository.rmsDao.getAllRmsData()

    fun addMessage(message : MessageItem){
        val messageList = chatMessages.value
        messageList?.add(message)
        chatMessages.value = messageList
    }

}
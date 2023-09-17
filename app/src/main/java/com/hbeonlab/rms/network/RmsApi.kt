package com.hbeonlab.rms.network

import com.hbeonlab.rms.data.models.ApiResponse
import com.hbeonlab.rms.data.models.RmsLogResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RmsApi {
    @GET("Web/bt.php")
    fun sendRmsData(@Query("f") params : String) : Call<ApiResponse>
}
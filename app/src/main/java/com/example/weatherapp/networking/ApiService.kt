package com.example.weatherapp.networking

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {


    @GET
    suspend fun getCityList(@Url url: String): Response<ResponseBody>

    @GET
    suspend fun getCurrentWeather(@Url url: String): Response<ResponseBody>

}
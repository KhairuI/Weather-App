package com.example.weatherapp.repository

import com.example.weatherapp.base.BaseRepository
import com.example.weatherapp.networking.APIService

class WeatherRepository(private val api: APIService) : BaseRepository() {


    // get city list
    suspend fun getCityList(url:String) = safeApiCall {

        api.getCityList(url)
    }

    // get current weather
    suspend fun getCurrentWeather(url:String) = safeApiCall {

        api.getCurrentWeather(url)
    }
}
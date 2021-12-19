package com.example.weatherapp.viewmodel.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.DataState
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {


    // get city list
    private val _getCityListResponse : MutableLiveData<DataState<Response<ResponseBody>>> = MutableLiveData()
    val getCityListResponse : LiveData<DataState<Response<ResponseBody>>> get() = _getCityListResponse

    fun getCityList(
        url:String
    ) = viewModelScope.launch {
        _getCityListResponse.value = DataState.Loading
        _getCityListResponse.value = repository.getCityList(url)
    }


    // get current weather
    private val _getCurrentWeatherResponse : MutableLiveData<DataState<Response<ResponseBody>>> = MutableLiveData()
    val getCurrentWeatherResponse : LiveData<DataState<Response<ResponseBody>>> get() = _getCurrentWeatherResponse

    fun getCurrentWeather(
        url:String
    ) = viewModelScope.launch {
        _getCurrentWeatherResponse.value = DataState.Loading
        _getCurrentWeatherResponse.value = repository.getCurrentWeather(url)
    }

}
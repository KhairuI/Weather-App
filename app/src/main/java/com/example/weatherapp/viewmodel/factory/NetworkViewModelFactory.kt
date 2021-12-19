package com.example.weatherapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.base.BaseRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewmodel.network.WeatherViewModel

@Suppress("UNCHECKED_CAST")
class NetworkViewModelFactory (
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(WeatherViewModel::class.java) -> WeatherViewModel(repository as WeatherRepository) as T

            else -> throw IllegalArgumentException("NetworkViewModelFactory Not Found")
        }
    }

}
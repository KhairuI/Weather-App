package com.example.weatherapp.models
import com.google.gson.annotations.SerializedName


data class ModelCurrentWeather(
    @SerializedName("base")
    val base: String?,
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("cod")
    val cod: Int?,
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: MainCurrentWeather?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("sys")
    val sys: SysCurrentWeather?,
    @SerializedName("timezone")
    val timezone: Int?,
    @SerializedName("visibility")
    val visibility: Int?,
    @SerializedName("weather")
    val weather: List<Weather>?,
    @SerializedName("wind")
    val wind: Wind?
)

data class MainCurrentWeather(
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("pressure")
    val pressure: Double?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    @SerializedName("temp_min")
    val tempMin: Double?
)

data class SysCurrentWeather(
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?,
    @SerializedName("type")
    val type: Int?
)

package com.example.weatherapp.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ModelCityList(
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("list")
    val list: List<SingleCity>?,
    @SerializedName("message")
    val message: String?
):Serializable

data class SingleCity (
    @SerializedName("clouds")
    val clouds: Clouds?,
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("dt")
    val dt: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: Main?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rain")
    val rain: Any?,
    @SerializedName("snow")
    val snow: Any?,
    @SerializedName("sys")
    val sys: Sys?,
    @SerializedName("weather")
    val weather: List<Weather>?,
    @SerializedName("wind")
    val wind: Wind?
):Serializable

data class Clouds(
    @SerializedName("all")
    val all: Int?
):Serializable

data class Coord(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?
):Serializable

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @SerializedName("grnd_level")
    val grndLevel: Double?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("pressure")
    val pressure: Double?,
    @SerializedName("sea_level")
    val seaLevel: Double?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("temp_max")
    val tempMax: Double?,
    @SerializedName("temp_min")
    val tempMin: Double?
):Serializable

data class Sys(
    @SerializedName("country")
    val country: String?
):Serializable

data class Weather(
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("main")
    val main: String?
):Serializable

data class Wind(
    @SerializedName("deg")
    val deg: Double?,
    @SerializedName("speed")
    val speed: Double?
):Serializable
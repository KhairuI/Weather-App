package com.example.weatherapp.core


class AppConstants {
    companion object {

        // Variables
        const val INTENT_FILTER= "intent_filter"
        const val BROADCAST_KEY= "broadcast_key"

        // Fragment names/tags =====================================================================
        const val FRAGMENT_SPLASH = "a"
        const val FRAGMENT_LIST = "b"
        const val FRAGMENT_DETAILS = "c"


        // API =====================================================================================

        const val API_URL = "http://api.openweathermap.org/"


        // ENDPOINT =====================================================================================

        const val ENDPOINT_GET_CITY_LIST = "data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76"


        fun imageLink(code:String):String{
            return "${API_URL}img/w/${code}.png"
        }

        fun getCurrentWeatherUrl(lat:Double,log:Double):String{
          return "${API_URL}data/2.5/weather?lat=${lat}&lon=${log}&appid=e384f9ac095b2109c751d95296f8ea76"

        }

    }
}
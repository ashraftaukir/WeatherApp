package com.taukir.weatherapp.cloud

import com.taukir.weatherapp.cloud.CloudDataConstant.WEATHER_API_ID
import com.taukir.weatherapp.cloud.CloudDataConstant.defaultLang
import com.taukir.weatherapp.models.WeatherResponse
import retrofit2.Response


class WeatherRepository {

    private val weatherService: WeatherService = WeatherApi.create()


    suspend fun getCurrentWeather(latitude:Double,longitude:Double): Response<WeatherResponse> {
        return weatherService.getCurrentWeatherUsingLatLang(latitude, longitude, WEATHER_API_ID)
    }

    suspend fun getCurrentWeatherUsingCity(city:String): Response<WeatherResponse> {
        return weatherService.getCurrentWeatherUsingCity(city, CloudDataConstant.UNITS, defaultLang,WEATHER_API_ID)
    }

}


package com.taukir.weatherapp.cloud

import com.taukir.weatherapp.cloud.CloudDataConstant.WEATHER_API_ID
import com.taukir.weatherapp.models.WeatherResponse
import retrofit2.Response


class WeatherRepository {

    private val weatherService: WeatherService = WeatherApi.create()


    suspend fun getCurrentWeather(): Response<WeatherResponse> {
        val latitude = 25.2048
        val longitude = 55.2708

        return weatherService.getCurrentWeather(latitude, longitude, WEATHER_API_ID)
    }
}


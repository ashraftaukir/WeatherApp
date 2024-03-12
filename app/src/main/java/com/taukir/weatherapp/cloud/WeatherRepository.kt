package com.taukir.weatherapp.cloud

import android.util.Log
import com.taukir.weatherapp.models.WeatherResponse
import retrofit2.Call
import retrofit2.Response


class WeatherRepository {

    private val weatherService: WeatherService = WeatherApi.create()


    suspend fun getCurrentWeather(): Response<WeatherResponse> {
        // Example: Get weather based on a predefined location (latitude, longitude)
        val latitude = 23.777176
        val longitude = 90.399452

        Log.d("hello_taukir", "getCurrentWeather: ")
        return weatherService.getCurrentWeather(latitude, longitude, "76b262df89f93cfdfe8ea647a38fcf5e")
    }
}


package com.taukir.weatherapp.cloud

import com.taukir.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

import com.taukir.weatherapp.cloud.CloudDataConstant.Weather

interface WeatherService {
    @GET(Weather)
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>


}


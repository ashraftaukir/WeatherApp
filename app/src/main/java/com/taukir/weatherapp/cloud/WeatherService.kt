package com.taukir.weatherapp.cloud

import com.taukir.weatherapp.cloud.CloudDataConstant.Weather
import com.taukir.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(Weather)
    suspend fun getCurrentWeatherUsingLatLang(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>


    @GET(Weather)
    suspend fun getCurrentWeatherUsingCity(
        @Query("q") q: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?,
        @Query("appid") appId: String?
    ):  Response<WeatherResponse>

}


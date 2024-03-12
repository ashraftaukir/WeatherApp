package com.taukir.weatherapp.cloud


object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun create(): WeatherService {
        return RetrofitClient.getClient(BASE_URL).create(WeatherService::class.java)
    }
}
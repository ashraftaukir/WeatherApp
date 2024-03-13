package com.taukir.weatherapp.models

data class WeatherResponse(
    val name: String,
    val wind: Wind,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val id: Int
)

data class Wind(
    val speed: Double,
)


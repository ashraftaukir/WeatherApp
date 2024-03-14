package com.taukir.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taukir.weatherapp.cloud.WeatherRepository
import com.taukir.weatherapp.models.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()
    val error = MutableLiveData<String>()
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather


    fun refreshWeather(latitude:Double,longitude:Double) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(latitude,longitude)
                handleResponse(response)
            } catch (e: Exception) {
                handleError(e.message ?: "Unknown error")
            }
        }
    }

    fun refreshWeatherUsingCity(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeatherUsingCity(city)
                handleResponse(response)
            } catch (e: Exception) {
                handleError(e.message ?: "Unknown error")
            }
        }
    }

    fun refreshWeatherUsingZipCode(zipCode: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeatherUsingZipCode(zipCode)
                handleResponse(response)
            } catch (e: Exception) {
                handleError(e.message ?: "Unknown error")
            }
        }
    }


    private fun handleResponse(response: Response<WeatherResponse>) {
        _weather.value = response.body()
    }

    private fun handleError(errorMessage: String) {
        error.value = errorMessage
    }
}

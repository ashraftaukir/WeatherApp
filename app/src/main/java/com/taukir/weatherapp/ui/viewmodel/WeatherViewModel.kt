package com.taukir.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taukir.weatherapp.cloud.WeatherRepository
import com.taukir.weatherapp.models.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    val weather = MutableLiveData<WeatherResponse?>()
    val error = MutableLiveData<String>()



    fun refreshWeather() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather()
                handleResponse(response)
            } catch (e: Exception) {
                handleError(e.message ?: "Unknown error")
            }
        }
    }

    private fun handleResponse(response: Response<WeatherResponse>) {
        weather.value = response.body()
    }

    private fun handleError(errorMessage: String) {
        error.value = errorMessage
    }
}

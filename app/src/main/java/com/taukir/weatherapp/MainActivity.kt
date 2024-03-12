package com.taukir.weatherapp
// MainActivity.kt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.taukir.weatherapp.models.WeatherResponse
import com.taukir.weatherapp.ui.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var locationTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var refreshButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationTextView = findViewById(R.id.locationTextView)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        refreshButton = findViewById(R.id.refreshButton)

        refreshButton.setOnClickListener {
            viewModel.refreshWeather()
        }

        viewModel.weather.observe(this) { weather ->
            updateUI(weather)
        }

        viewModel.error.observe(this) {
            // Handle error
        }

        viewModel.refreshWeather()
    }

    private fun updateUI(weather: WeatherResponse?) {
        if (weather != null) {
            locationTextView.text = weather.name
            temperatureTextView.text = "${weather.main.temp}°C"
            descriptionTextView.text = weather.weather[0].description
        }
    }
}

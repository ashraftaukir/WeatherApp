package com.taukir.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.taukir.weatherapp.R
import com.taukir.weatherapp.models.WeatherResponse
import com.taukir.weatherapp.utils.getWeatherAnimation

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var humidityTextSwitcher: TextSwitcher
    private lateinit var windTextSwitcher: TextSwitcher
    private lateinit var tempTextSwitcher: TextSwitcher
    private lateinit var descriptionTextSwitcher: TextSwitcher
    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setUpSwitcher()
        viewModel.weather.observe(this) { weather ->
            updateUI(weather)
        }

        viewModel.error.observe(this) {
            // Handle error
        }

        viewModel.refreshWeather()
    }

    private fun initViews() {
        humidityTextSwitcher = findViewById(R.id.humidity_text_view)
        windTextSwitcher = findViewById(R.id.wind_text_view)
        tempTextSwitcher = findViewById(R.id.temp_text_view)
        descriptionTextSwitcher = findViewById(R.id.description_text_view)
        animationView = findViewById(R.id.animation_view)
    }


    private fun setUpSwitcher(){

        tempTextSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 40f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }
        descriptionTextSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 25f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }

        humidityTextSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }

        windTextSwitcher.setFactory {
            val textView = TextView(this)
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }
    }


    private fun updateUI(weather: WeatherResponse?) {

        if (weather != null) {
            Log.d("check_weather_json", weather.toString())
            descriptionTextSwitcher.setText(weather.weather[0].description)
            tempTextSwitcher.setText("${weather.main.temp}°")
            humidityTextSwitcher.setText("${weather.main.humidity}%")
            windTextSwitcher.setText("${weather.wind.speed} km/hr")

            animationView.setAnimation(
                getWeatherAnimation(
                    weather.weather[0].id
                )
            )
            animationView.playAnimation()
        }
    }
}

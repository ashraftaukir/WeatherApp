package com.taukir.weatherapp.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.taukir.weatherapp.R
import com.taukir.weatherapp.models.WeatherResponse
import com.taukir.weatherapp.ui.viewmodel.WeatherViewModel
import com.taukir.weatherapp.utils.getWeatherAnimation

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var humidityTextSwitcher: TextSwitcher
    private lateinit var searchViewImg: ImageView
    private lateinit var cityEditText: EditText
    private lateinit var windTextSwitcher: TextSwitcher
    private lateinit var tempTextSwitcher: TextSwitcher
    private lateinit var descriptionTextSwitcher: TextSwitcher
    private lateinit var animationView: LottieAnimationView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up location
        setUpLocation()

        //initial views
        initViews()

        //initial switcher
        setUpSwitcher()

        viewModel.weather.observe(this) { weather ->
            updateUI(weather)
        }

        viewModel.error.observe(this) {
            // Handle error
        }

    }

    private fun setUpLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check for permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation()
        } else {
            // Request permissions
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun initViews() {
        humidityTextSwitcher = findViewById(R.id.humidity_text_view)
        windTextSwitcher = findViewById(R.id.wind_text_view)
        tempTextSwitcher = findViewById(R.id.temp_text_view)
        descriptionTextSwitcher = findViewById(R.id.description_text_view)
        animationView = findViewById(R.id.animation_view)
        searchViewImg = findViewById(R.id.searchViewImg)
        cityEditText = findViewById(R.id.cityEditText)
        searchViewImg.setOnClickListener {
            if(cityEditText.text.toString().isNotEmpty()){
                viewModel.refreshWeatherUsingCity(cityEditText.text.toString())
            }
        }
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

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 100
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    viewModel.refreshWeather(latitude,longitude)

                } else {
                    showToast("No location available")
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                showToast("Error getting location: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation()
            } else {
                showToast("Location permission denied")
            }
        }
    }
}

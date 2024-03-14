package com.taukir.weatherapp.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.taukir.weatherapp.R
import com.taukir.weatherapp.databinding.ActivityMainBinding
import com.taukir.weatherapp.models.WeatherResponse
import com.taukir.weatherapp.ui.viewmodel.WeatherViewModel
import com.taukir.weatherapp.utils.getWeatherAnimation

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mainSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: ActivityMainBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set up location
        setUpLocation()

        //initial Listeners
        initListeners()

        //initial switcher
        setUpSwitcher()

        viewModel.weather.observe(this) { weather ->
            updateUI(weather)
        }

        viewModel.error.observe(this) {
            // Handle error
            showToast(it)
        }

    }

    private fun setUpLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check for permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else {
            // Request permissions
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun initListeners() {

        binding.toolbarLayout.citySearchViewImg.setOnClickListener {
            if (binding.toolbarLayout.cityEditText.text.toString().isNotEmpty()) {
                viewModel.refreshWeatherUsingCity(binding.toolbarLayout.cityEditText.text.toString())
            }else{
                showToast("Empty city")
            }
        }

        binding.toolbarLayout.zipCodeSearchViewImg.setOnClickListener {
            if (binding.toolbarLayout.zipCodeEditText.text.toString().isNotEmpty()) {
                viewModel.refreshWeatherUsingZipCode(binding.toolbarLayout.zipCodeEditText.text.toString())
            }else{
                showToast("Empty zip code")
            }
        }

        mainSwipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshWeather(latitude, longitude)
            mainSwipeRefreshLayout.isRefreshing = false

        }

    }


    private fun setUpSwitcher() {

        binding.contentMainLayout.tempTextView.setFactory {
            val textView = TextView(this)
            textView.textSize = 40f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }
        binding.contentMainLayout.descriptionTextView.setFactory {
            val textView = TextView(this)
            textView.textSize = 25f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }

        binding.contentMainLayout.humidityTextView.setFactory {
            val textView = TextView(this)
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }

        binding.contentMainLayout.windTextView.setFactory {
            val textView = TextView(this)
            textView.textSize = 16f
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_10))
            textView
        }
    }


    private fun updateUI(weather: WeatherResponse?) {

        if (weather != null) {
            binding.contentMainLayout.currentCity.text = weather.name
            binding.contentMainLayout.descriptionTextView.setText(weather.weather[0].description)
            binding.contentMainLayout.tempTextView.setText("${weather.main.temp}°")
            binding.contentMainLayout.humidityTextView.setText("${weather.main.humidity}%")
            binding.contentMainLayout.windTextView.setText("${weather.wind.speed} km/hr")

            binding.contentMainLayout.animationView.setAnimation(
                getWeatherAnimation(
                    weather.weather[0].id
                )
            )
            binding.contentMainLayout.animationView.playAnimation()
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
                    latitude = location.latitude
                    longitude = location.longitude
                    viewModel.refreshWeather(latitude, longitude)

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

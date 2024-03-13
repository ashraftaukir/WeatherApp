package com.taukir.weatherapp.utils

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.taukir.weatherapp.R

fun getWeatherAnimation(weatherCode: Int): Int {
    if (weatherCode / 100 == 2) {
        return R.raw.storm_weather
    } else if (weatherCode / 100 == 3) {
        return R.raw.rainy_weather
    } else if (weatherCode / 100 == 5) {
        return R.raw.rainy_weather
    } else if (weatherCode / 100 == 6) {
        return R.raw.snow_weather
    } else if (weatherCode / 100 == 7) {
        return R.raw.unknown
    } else if (weatherCode == 800) {
        return R.raw.clear_day
    } else if (weatherCode == 801) {
        return R.raw.few_clouds
    } else if (weatherCode == 803) {
        return R.raw.broken_clouds
    } else if (weatherCode / 100 == 8) {
        return R.raw.cloudy_weather
    }
    return R.raw.unknown
}

fun isRTL(context: Context): Boolean {
    val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
    val directionality = Character.getDirectionality(locale!!.displayName[0]).toInt()
    return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
}
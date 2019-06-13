package com.app.avy.module

object WeatherData {
    var mCurrentWeather: CurrentWeather? = null

    fun setCurrentWeather(currentWeather: CurrentWeather) {
        mCurrentWeather = currentWeather
    }

    fun getCurrentWeather(): CurrentWeather? {
        return mCurrentWeather
    }

}
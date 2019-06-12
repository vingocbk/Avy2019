package com.app.avy.module

data class CurrentWeather(
    var coord: Coord,
    var weather: ArrayList<Weather>,
    var base: String?,
    var main: Main,
    var visibility: Int,
    var wind: Wind,
    var clouds: Clouds,
    var sys: Sys,
    var name: String
)


data class Coord(
    var lon: Float,
    var lat: Float
)

data class Weather(
    var main: String,
    var description: String
)

data class Main(
    var temp: Int,
    var pressure: Int,
    var humidity: Int,
    var temp_min: Int,
    var temp_max: Int
)

data class Wind(
    var speed: Float,
    var deg: Int
)

data class Clouds(var all: Int)

data class Sys(
    var type: Int,
    var message: Float,
    var country: String,
    var sunrise: Long,
    var sunset: Long
)

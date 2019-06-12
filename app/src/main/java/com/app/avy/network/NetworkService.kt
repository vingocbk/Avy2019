package com.app.avy.network

import com.app.avy.module.CurrentWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("weather")
    fun getCurrentWeather(@Query("lat") lat: Double,@Query("lon") lon: Double, @Query("units") units: String, @Query("APPID") apiKey: String): Observable<CurrentWeather>
}
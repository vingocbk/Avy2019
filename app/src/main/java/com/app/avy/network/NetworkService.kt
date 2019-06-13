package com.app.avy.network

import com.app.avy.module.ColorRequest
import com.app.avy.module.CurrentWeather
import com.app.avy.module.LightRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkService {
    @GET("weather")
    fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("units") units: String, @Query("APPID") apiKey: String): Observable<CurrentWeather>


    @POST("control_intensity")
    fun setOpacity(@Body request: LightRequest): Observable<String>


    @POST("/control_led_color")
    fun chanegColor(@Body request: ColorRequest): Observable<String>

}
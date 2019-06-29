package com.app.avy.network

import com.app.avy.module.*
import io.reactivex.Observable
import retrofit2.http.*

interface NetworkService {

    @GET("BhSvb9Bt")
    fun getConfig(): Observable<ConfigModule>

    @GET("weather")
    fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("units") units: String, @Query("APPID") apiKey: String): Observable<CurrentWeather>

    @POST("control_intensity")
    fun setOpacity(@Body request: LightRequest): Observable<String>

    @POST("/control_led_color")
    fun chanegColor(@Body request: ColorRequest): Observable<String>

    @POST("/setmoderun")
    fun chanegSpeed(@Body request: SpeedModule): Observable<Any>

    @POST("/config")
    fun configDevice(@Body request: ConfigDevice): Observable<Any>

    @POST("/setlowspeed")
    fun changeSlow(@Body request: SlowModule): Observable<Any>

    @GET("/resetdistant")
    fun changeDistant(): Observable<Any>

    @POST("/settimereturn")
    fun changeTime(@Body request: TimeModule): Observable<Any>

    @GET("/open")
    fun openWindow(): Observable<Any>

    @GET("/close")
    fun closeWindow(): Observable<Any>

    @POST("/control_led")
    fun changeLight(@Body request: LightModule): Observable<Any>

}
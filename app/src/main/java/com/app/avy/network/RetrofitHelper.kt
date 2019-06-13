package com.app.avy.network

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitHelper {


    companion object {
        private var instance: RetrofitHelper? = null

        fun getInstance(): RetrofitHelper {
            if (instance == null) {
                synchronized(RetrofitHelper::class.java) {
                    if (instance == null) {
                        instance = RetrofitHelper()
                    }
                }
            }
            return instance!!
        }
    }

    fun getNetworkService(url: String): NetworkService {
        val retrofit = createRetrofit(url)
        return retrofit.create(NetworkService::class.java!!)
    }

    /**
     * Creates a pre configured Retrofit instance
     */
    private fun createRetrofit(url: String): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient.build())
            .build()
    }

}
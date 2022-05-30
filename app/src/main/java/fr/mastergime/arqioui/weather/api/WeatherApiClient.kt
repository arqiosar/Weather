package fr.mastergime.arqioui.weather.api

import fr.mastergime.arqioui.weather.interceptors.NetworkConnectionInterceptor
import fr.mastergime.arqioui.weather.interceptors.RequestInterceptor
import fr.mastergime.arqioui.weather.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiClient {

    val api = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(WeatherApi::class.java)


    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RequestInterceptor())
        client.addInterceptor(NetworkConnectionInterceptor())
        return client.build()
    }



}
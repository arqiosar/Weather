package fr.mastergime.arqioui.weather.api

import fr.mastergime.arqioui.weather.models.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?")
    fun getWeatherByGPS(@Query("lat") latitude: String,
                        @Query("lon") longitude: String,
                        @Query("units") units: String)
    : Single<WeatherResponse>

    @GET("weather?")
    fun getData(@Query("q") cityName: String,
                @Query("units") metric: String,
                @Query("APPID") APPID: String
    ): Single<WeatherResponse>
}
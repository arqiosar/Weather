package fr.mastergime.arqioui.weather.repository

import fr.mastergime.arqioui.weather.api.WeatherApiClient
import fr.mastergime.arqioui.weather.models.WeatherResponse
import io.reactivex.Single

class RequestRepository {

    fun getDataFromGps(
        latitude: String,
        longitude: String,
        units: String
    ): Single<WeatherResponse> {
        return WeatherApiClient.api.getWeatherByGPS(latitude, longitude, units)
    }

    fun getDataService(cityName: String): Single<WeatherResponse> {
        return WeatherApiClient.api.getData(cityName)
    }
}
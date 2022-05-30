package fr.mastergime.arqioui.weather.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.mastergime.arqioui.weather.models.SearchWeatherByCityViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository

class SearchCityWeatherFactory(
    private val repository: RequestRepository
    ) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchWeatherByCityViewModel(repository) as T
    }
}
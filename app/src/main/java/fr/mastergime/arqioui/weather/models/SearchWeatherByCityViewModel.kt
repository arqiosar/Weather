package fr.mastergime.arqioui.weather.models

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SearchWeatherByCityViewModel(private val repository: RequestRepository) : ViewModel(){
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherResponse>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_loading = MutableLiveData<Boolean>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {

        weather_loading.value = true
        disposable.add(
            repository.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {

                    override fun onSuccess(t: WeatherResponse) {
                        weather_data.value = t
                        weather_error.value = false
                        weather_loading.value = false
                        Log.d(TAG, "onSuccess: Success")
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value = true
                        weather_loading.value = false
                        Log.e(TAG, "onError: " + e)
                    }
                })
        )

    }
}
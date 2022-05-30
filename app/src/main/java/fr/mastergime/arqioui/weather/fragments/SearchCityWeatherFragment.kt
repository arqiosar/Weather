package fr.mastergime.arqioui.weather.fragments

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.Converters
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import fr.mastergime.arqioui.weather.R
import fr.mastergime.arqioui.weather.databinding.FragmentSearchCityWeatherBinding
import fr.mastergime.arqioui.weather.factory.SearchCityWeatherFactory
import fr.mastergime.arqioui.weather.models.SearchWeatherByCityViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository
import fr.mastergime.arqioui.weather.util.dateConverter
import fr.mastergime.arqioui.weather.util.timeConverter
import java.text.SimpleDateFormat
import java.util.*

class SearchCityWeatherFragment : Fragment() {

    private val SWCViewModel: SearchWeatherByCityViewModel by viewModels(factoryProducer = {
        SearchCityWeatherFactory(
            RequestRepository()
        )
    })

    private lateinit var _SWCBinding: FragmentSearchCityWeatherBinding


    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _SWCBinding = FragmentSearchCityWeatherBinding.inflate(inflater)
        return _SWCBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GET = context?.getSharedPreferences("Weather", MODE_PRIVATE)!!
        SET = GET.edit()

        var cName = GET.getString("cityName", "nice")?.toLowerCase()
        _SWCBinding.edtCityName.setText(cName)
        SWCViewModel.refreshData(cName!!)

        getLiveData()

        _SWCBinding.swipeRefreshLayout.setOnRefreshListener {
            _SWCBinding.lytLocation.visibility = View.GONE
            _SWCBinding.locationLoading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.toLowerCase()
            _SWCBinding.edtCityName.setText(cityName)
            SWCViewModel.refreshData(cityName!!)
            _SWCBinding.swipeRefreshLayout.isRefreshing = false
        }

        _SWCBinding.imgSearchCity.setOnClickListener {
            val cityName = _SWCBinding.edtCityName.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            SWCViewModel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: $cityName")
        }

    }

    private fun getLiveData() {
        SWCViewModel.weather_data.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                _SWCBinding.lytLocation.visibility = View.VISIBLE

                _SWCBinding.tvState.text = data.sys?.country.toString()
                _SWCBinding.tvCityName.text = data.name.toString()

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + (data.weather?.get(0)?.icon!!) + "@2x.png")
                    .into(_SWCBinding.imgState)

                _SWCBinding.tvTemperature.text = data.main?.temp.toString()
                _SWCBinding.tvHumidity.text = data.main?.humidity.toString()
                _SWCBinding.tvPressure.text = data.main?.pressure.toString()
                _SWCBinding.tvVisibility.text = data.visibility.toString()
                _SWCBinding.tvWind.text = data.wind?.speed.toString()
                _SWCBinding.tvSunrise.text = data.sys?.sunrise?.let { it1 -> timeConverter(it1.toLong()) }
                _SWCBinding.tvSunset.text = data.sys?.sunset?.let { it1 -> timeConverter(it1.toLong()) }
                _SWCBinding.tvDate.text = dateConverter()

            }
        })

        SWCViewModel.weather_error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (error) {
                    Log.d("ERROR", "")
                    _SWCBinding.locationLoading.visibility = View.GONE
                    _SWCBinding.lytLocation.visibility = View.GONE
                } else {
                }
            }
        })

        SWCViewModel.weather_loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (loading) {
                    _SWCBinding.locationLoading.visibility = View.VISIBLE
                    _SWCBinding.lytLocation.visibility = View.GONE
                } else {
                    _SWCBinding.locationLoading.visibility = View.GONE
                }
            }
        })
    }




}
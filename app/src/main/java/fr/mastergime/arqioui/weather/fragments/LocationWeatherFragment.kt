package fr.mastergime.arqioui.weather.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.content.Context
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import fr.mastergime.arqioui.weather.databinding.FragmentLocationWeatherBinding
import fr.mastergime.arqioui.weather.models.WeatherViewModel
import fr.mastergime.arqioui.weather.util.Constants
import android.Manifest
import androidx.fragment.app.viewModels
import fr.mastergime.arqioui.weather.factory.LocationWeatherFactory
import fr.mastergime.arqioui.weather.factory.SearchCityWeatherFactory
import fr.mastergime.arqioui.weather.models.SearchWeatherByCityViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository

class LocationWeatherFragment : Fragment(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private val LWViewModel: WeatherViewModel by viewModels(factoryProducer = {
        LocationWeatherFactory(
            RequestRepository()
        )
    })
    private lateinit var _LWBinding: FragmentLocationWeatherBinding

    private var latitude: String? = null
    private var longitude: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _LWBinding = FragmentLocationWeatherBinding.inflate(inflater)
        return _LWBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocation()

        LWViewModel.getWeatherDataWithGPS(latitude!!, longitude!!, Constants.METRIC)

        LWViewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                _LWBinding.lytLocation.visibility = View.VISIBLE
                //_LWBinding.locationGPS = locationGps
                _LWBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                //_LWBinding.tvDate.text = dateConverter()
                //_LWBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                //_LWBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
                _LWBinding.imgState.setImageResource(resources.getIdentifier("ic_"+locationGps.weather?.get(0)?.icon, "drawable", view.context.packageName))

            }
        })

        LWViewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    //locationLoading.visibility = View.VISIBLE
                    //lytLocation.visibility = View.GONE
                }else{
                    //locationLoading.visibility = View.GONE
                }
            }
        })
    }

    private fun getLocation() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((this.context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
        } != PackageManager.PERMISSION_GRANTED)) {
            this.activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Location get Successfully", Toast.LENGTH_SHORT).show()
                getWeatherForCurrentLocation()
            } else {
                //user denied the permission
            }
        }
    }

    private fun getWeatherForCurrentLocation() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude.toString()
                val logitude = location.longitude.toString()
                val params: RequestParams = RequestParams();
                params.put("lat", latitude)
                params.put("lon", logitude)
                params.put("appid", app_id)
                letsDoSomeNetworking(params)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderDisabled(provider: String) {

            }

            override fun onProviderEnabled(provider: String) {

            }
    }*/


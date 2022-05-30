package fr.mastergime.arqioui.weather.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import fr.mastergime.arqioui.weather.databinding.FragmentLocationWeatherBinding
import fr.mastergime.arqioui.weather.factory.CurrentLocationWeatherFactory
import fr.mastergime.arqioui.weather.models.CurrentLocationWeatherViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository
import fr.mastergime.arqioui.weather.util.Constants
import fr.mastergime.arqioui.weather.util.dateConverter
import fr.mastergime.arqioui.weather.util.timeConverter


class CurrentWeatherLocationFragment : Fragment(), LocationListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val LWViewModel: CurrentLocationWeatherViewModel by viewModels(factoryProducer = {
        CurrentLocationWeatherFactory(
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        getLocation()
        Toast.makeText(this.context, "$longitude", Toast.LENGTH_SHORT).show()

        LWViewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                _LWBinding.lytLocation.visibility = View.VISIBLE
                _LWBinding.tvState.text = locationGps.sys?.country.toString()
                _LWBinding.tvCityName.text = locationGps.name.toString()
                _LWBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                _LWBinding.tvHumidity.text = locationGps.main!!.humidity.toString()
                _LWBinding.tvPressure.text = locationGps.main!!.pressure.toString()
                _LWBinding.tvWind.text = locationGps.wind?.speed.toString()
                _LWBinding.tvVisibility.text = locationGps.visibility.toString()
                _LWBinding.tvDate.text = dateConverter()
                _LWBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                _LWBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + (locationGps.weather?.get(0)?.icon!!) + "@2x.png")
                    .into(_LWBinding.imgState)

            }
        })

        LWViewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    _LWBinding.locationLoading.visibility = View.VISIBLE
                    _LWBinding.lytLocation.visibility = View.GONE
                } else {
                    _LWBinding.locationLoading.visibility = View.GONE
                }
            }
        })
    }

    private fun getLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()){
                if(this.context?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                 != PackageManager.PERMISSION_GRANTED && this.context?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    } != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermission()
                    return
                }

                this.activity?.let {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener(it){ task ->
                        val location: Location?=task.result
                        if(location == null){
                            Toast.makeText(context, "Null Recieved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Get Success", Toast.LENGTH_SHORT).show()

                            Toast.makeText(context, "${location.longitude}", Toast.LENGTH_SHORT).show()
                            LWViewModel.getWeatherDataWithGPS(location.latitude.toString(), location.longitude.toString(), Constants.METRIC)
                            latitude = location.latitude.toString()
                            longitude = location.longitude.toString()

                        }

                    }
                }

            } else{
                Toast.makeText(context, "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }


    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermission(): Boolean{
        if(this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission(){
        this.activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, "Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }
            else{
                Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }
}




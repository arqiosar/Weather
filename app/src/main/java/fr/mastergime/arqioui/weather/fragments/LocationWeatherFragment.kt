package fr.mastergime.arqioui.weather.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import fr.mastergime.arqioui.weather.databinding.FragmentLocationWeatherBinding
import fr.mastergime.arqioui.weather.factory.LocationWeatherFactory
import fr.mastergime.arqioui.weather.models.WeatherViewModel
import fr.mastergime.arqioui.weather.repository.RequestRepository


class LocationWeatherFragment : Fragment(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        getLocation()
        Toast.makeText(this.context, "$longitude", Toast.LENGTH_SHORT).show()

        //LWViewModel.getWeatherDataWithGPS(latitude!!, longitude!!, Constants.METRIC)

        LWViewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                _LWBinding.lytLocation.visibility = View.VISIBLE
                //_LWBinding.locationGPS = locationGps
                _LWBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                //_LWBinding.tvDate.text = dateConverter()
                //_LWBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                //_LWBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
                _LWBinding.imgState.setImageResource(
                    resources.getIdentifier(
                        "ic_" + locationGps.weather?.get(
                            0
                        )?.icon, "drawable", view.context.packageName
                    )
                )

            }
        })

        LWViewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    //locationLoading.visibility = View.VISIBLE
                    //lytLocation.visibility = View.GONE
                } else {
                    //locationLoading.visibility = View.GONE
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
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                 != PackageManager.PERMISSION_GRANTED && this.context?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
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
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission(){
        this.activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
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




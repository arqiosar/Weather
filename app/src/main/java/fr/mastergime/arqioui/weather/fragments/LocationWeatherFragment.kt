package fr.mastergime.arqioui.weather.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.mastergime.arqioui.weather.databinding.FragmentLocationWeatherBinding
import fr.mastergime.arqioui.weather.models.WeatherViewModel


class LocationWeatherFragment : Fragment() {

    private val REQUEST_CODE = 1

    private lateinit var viewModel: WeatherViewModel
    private lateinit var _LWBinding: FragmentLocationWeatherBinding

    lateinit var mLocationManager: LocationManager
    lateinit var mLocationListener: LocationListener

    //var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null

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
    }
}
/*viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

location = SimpleLocation(context)
if (!location!!.hasLocationEnabled()) {
    SimpleLocation.openSettings(context)
} else {
    if (ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    } else {
        location = SimpleLocation(context)
        latitude = String.format("%.6f", location?.latitude)
        longitude = String.format("%.6f", location?.longitude)
        Log.e("LAT1", "" + latitude)
        Log.e("LONG1", "" + longitude)

    }
}
viewModel.getWeatherDataWithGPS(latitude!!, longitude!!, Constant.METRIC)

viewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
    locationGps?.let {
        _LWBinding.lytLocation.visibility = View.VISIBLE
        _LWBinding.locationGPS = locationGps
        _LWBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
        _LWBinding.tvDate.text = dateConverter()
        _LWBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
        _LWBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
        _LWBinding.imgState.setImageResource(resources.getIdentifier("ic_"+locationGps.weather?.get(0)?.icon, "drawable", view.context.packageName))

    }
})

/*viewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
    loading?.let {
        if (it){
            locationLoading.visibility = View.VISIBLE
            lytLocation.visibility = View.GONE
        }else{
            locationLoading.visibility = View.GONE       }
    }
})*/
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
}

 */

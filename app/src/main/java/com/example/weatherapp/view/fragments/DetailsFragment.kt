package com.example.weatherapp.view.fragments

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.core.AppConstants
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.models.SingleCity
import com.example.weatherapp.networking.APIService
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.PermissionUtils
import com.example.weatherapp.viewmodel.network.WeatherViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class DetailsFragment : BaseFragment<WeatherViewModel, FragmentDetailsBinding, WeatherRepository>(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener {


    override fun getViewModel() = WeatherViewModel::class.java
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentDetailsBinding.inflate(inflater, container, false)
    override fun getRepository() = WeatherRepository(remoteDataSource.buildApi(APIService::class.java))

    companion object {
        fun newInstance(singleCity: SingleCity): DetailsFragment {
            val args = Bundle()
            val fragment = DetailsFragment()
            args.putSerializable("city",singleCity)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var singleCity: SingleCity
    private lateinit var mMap:GoogleMap
    private val args:DetailsFragmentArgs by navArgs()

    override fun init(savedInstanceState: Bundle?) {

        binding.topToolbar.imgAlarm.visibility= View.GONE
        // back
        binding.topToolbar.imgBack.setOnClickListener {

            val action= DetailsFragmentDirections.actionDetailsFragmentToListFragment()
            findNavController().navigate(action)
        }

        singleCity= args.singlecity

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setData(singleCity)


    }

    @SuppressLint("SetTextI18n")
    private fun setData(singleCity: SingleCity) {

        binding.txtName.text= singleCity.name
        binding.txtDesc.text= singleCity.weather?.get(0)?.description
        binding.txtHumidity.text= "${resources.getString(R.string.txt_humidity)} ${singleCity.main?.humidity}"
        binding.txtWindSpeed.text= "${resources.getString(R.string.txt_wind_speed)} ${singleCity.wind?.speed}"
        binding.maxTxtTemp.text= "${singleCity.main?.tempMax?.minus(273)?.toInt()}"
        binding.minTxtTemp.text= "${singleCity.main?.tempMin?.minus(273)?.toInt()}"

        Glide.with(mContext).load(AppConstants.imageLink(singleCity.weather?.get(0)?.icon.toString())).into(binding.imgIcon)


    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {

        mMap= googleMap
       // mMap.uiSettings.isZoomControlsEnabled= true
        mMap.uiSettings.isMyLocationButtonEnabled= false
        mMap.setOnMarkerClickListener(this)
        setMap()

    }

    private fun setMap() {
        if(PermissionUtils.checkPermission(requireContext())){
            val currentLatLang= LatLng(singleCity.coord?.lat!!, singleCity.coord?.lon!!)
            addMarker(currentLatLang)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLang,15f))
        }
    }

    private fun addMarker(currentLatLang: LatLng) {
        /*val address= getCityName(currentLatLang)
        val city= address?.get(0)?.getAddressLine(0).toString()*/
        val markerOption= MarkerOptions().position(currentLatLang).title(singleCity.name)
        mMap.addMarker(markerOption)
    }

    private fun getCityName(latLng: LatLng): MutableList<Address>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        Log.d("mymsg", "getCityName: ${ geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5)}")
        return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5)
    }

    override fun onMarkerClick(p0: Marker)= false
}
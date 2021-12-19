package com.example.weatherapp.view.fragments

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.core.AppConstants
import com.example.weatherapp.databinding.FragmentListBinding
import com.example.weatherapp.models.ModelCityList
import com.example.weatherapp.models.ModelCurrentWeather
import com.example.weatherapp.models.SingleCity
import com.example.weatherapp.networking.APIService
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.AlarmReceiver
import com.example.weatherapp.utils.DataState
import com.example.weatherapp.utils.LoadingDialog
import com.example.weatherapp.utils.PermissionUtils
import com.example.weatherapp.view.adapter.CityListAdapter
import com.example.weatherapp.viewmodel.network.WeatherViewModel
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.target.CustomTarget
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class ListFragment : BaseFragment<WeatherViewModel, FragmentListBinding, WeatherRepository>() {


    override fun getViewModel() = WeatherViewModel::class.java
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentListBinding.inflate(inflater, container, false)
    override fun getRepository() = WeatherRepository(remoteDataSource.buildApi(APIService::class.java))

    companion object {
        fun newInstance(): ListFragment {
            val args = Bundle()
            val fragment = ListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun init(savedInstanceState: Bundle?) {

        // invisible back button
        binding.topToolbar.imgBack.visibility= View.GONE

        // loading
        loadingDialog = LoadingDialog(mContext)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(mContext)

        // observe all api response
        observeCityListResponse()
        observerCurrentWeather()

        // get City List
        getCityList()
        createNotificationChannel()

        binding.topToolbar.imgAlarm.setOnClickListener {
            if(PermissionUtils.checkPermission(mContext)){
                if(PermissionUtils.isGpsEnable(mContext)){
                    pickTime()
                }
                else{
                    PermissionUtils.enableGps(mContext)
                }
            }

        }

    }

    private fun pickTime() {
        picker= MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set weather alarm time")
            .build()
        picker.show(childFragmentManager,"weatherApp")
        picker.addOnPositiveButtonClickListener {

            if(picker.hour>12){

                val time= "${String.format("%02d",picker.hour-12)} : ${String.format("%02d",picker.minute)} PM"
                Snackbar.make(requireView(),"Alert set for $time",Snackbar.LENGTH_SHORT).show()
            }
            else{

                val time= "${String.format("%02d",picker.hour)} : ${String.format("%02d",picker.minute)} AM"
                Snackbar.make(requireView(),"Alert set for $time",Snackbar.LENGTH_SHORT).show()
            }

            calendar= Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY]= picker.hour
            calendar[Calendar.MINUTE]= picker.minute
            calendar[Calendar.SECOND]= 0
            calendar[Calendar.MILLISECOND]= 0
            setAlarm()
        }


    }

    @SuppressLint("MissingPermission")
    private fun setAlarm() {

        alarmManager= mContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent= Intent(mContext,AlarmReceiver::class.java)
        pendingIntent= PendingIntent.getBroadcast(mContext,0,intent,0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel("channelId","WeatherApp",NotificationManager.IMPORTANCE_HIGH)
            channel.description="Description of Weather App"

            val notificationManager = mContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun getMyLocation() {
        if(PermissionUtils.checkPermission(mContext)){
            Log.d("mymsg", "On Create: Permission Granted")
            if(PermissionUtils.isGpsEnable(requireContext())){
                Log.d("mymsg", "On Create: GPS already enable")
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location= it.result
                    if(location== null){
                        newLocation()
                    }
                    else{
                        viewModel.getCurrentWeather(AppConstants.getCurrentWeatherUrl(location.latitude,location.longitude))
                    }
                }

            }
            else{
                PermissionUtils.enableGps(mContext)
            }
        }
    }

    private fun newLocation(){
        locationRequest= LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority= LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates= 2
        }
        if(PermissionUtils.checkPermission(context)){
            Looper.myLooper()?.let {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,locationCallBack, it
                )
            }
        }


    }

    private val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {

            val lastLocation= p0.lastLocation
            viewModel.getCurrentWeather(AppConstants.getCurrentWeatherUrl(lastLocation.latitude,lastLocation.longitude))

        }
    }

    private fun getCityList() {

        viewModel.getCityList("${AppConstants.API_URL}${AppConstants.ENDPOINT_GET_CITY_LIST}")

    }

    private fun observerCurrentWeather() {

        viewModel.getCurrentWeatherResponse.observe(viewLifecycleOwner,{


            when(it){
                is DataState.Success -> {

                    try {

                        // gone loading
                        //if (loadingDialog.isShowing) loadingDialog.dismiss()

                        // get data
                        val body = it.value.body()?.string()
                        if (!body.isNullOrBlank()) {
                            val jsonObject = JSONObject(body)
                            Log.d("mymsg", "City List: $jsonObject")
                           // setList(jsonObject)
                            setNotification(jsonObject)

                        } else {
                            Log.e("mymsg", "observeShopDetailResponse: ${it.value.errorBody()?.string()}")
                        }
                        Log.e("mymsg", "${body.toString()}")

                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                is DataState.Loading -> {
                    //if (!loadingDialog.isShowing) loadingDialog.show()
                }

                is DataState.Error -> {
                    if (it.isNetworkError) Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show()
                    Log.e("#X_API_RES", "$it")
                }
            }
        })
    }

    private fun observeCityListResponse() {

        viewModel.getCityListResponse.observe(viewLifecycleOwner,{

            when(it){
                is DataState.Success -> {

                    try {

                        // gone loading
                        if (loadingDialog.isShowing) loadingDialog.dismiss()

                        // get data
                        val body = it.value.body()?.string()
                        if (!body.isNullOrBlank()) {
                            val jsonObject = JSONObject(body)
                            Log.d("mymsg", "City List: $jsonObject")
                            setList(jsonObject)

                        } else {
                            Log.e("mymsg", "observeShopDetailResponse: ${it.value.errorBody()?.string()}")
                        }
                        Log.e("mymsg", " shop category: ${body.toString()}")

                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                is DataState.Loading -> {
                    if (!loadingDialog.isShowing) loadingDialog.show()
                }

                is DataState.Error -> {
                    if (it.isNetworkError) Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show()
                    Log.e("#X_API_RES", "$it")
                }
            }
        })
    }


    @SuppressLint("RemoteViewLayout")
    private fun setNotification(jsonObject: JSONObject) {

        val currentWeather = Gson().fromJson(jsonObject.toString(), ModelCurrentWeather::class.java)
        var image: Bitmap? = null
        val temperature= currentWeather.main?.temp?.minus(273)?.toInt()
        val builder= NotificationCompat.Builder(mContext,"channelId")


        Glide.with(mContext).asBitmap().load(AppConstants.imageLink(currentWeather.weather?.get(0)?.icon.toString()))
            .into(object :CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    image= resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })

        val contentView= RemoteViews(mContext.packageName,R.layout.notification_view)
        contentView.apply {
            setTextViewText(R.id.txt_temp_title, "Current Temperature: ${temperature.toString()}°c")
            setImageViewBitmap(R.id.img_temp,image)

        }

        builder.apply {
                  setSmallIcon(R.drawable.ic_launcher_background)
                  setAutoCancel(true)
                  setDefaults(NotificationCompat.DEFAULT_ALL)
                  priority = NotificationCompat.PRIORITY_HIGH
                 setCustomContentView(contentView)
              }


        val manager= NotificationManagerCompat.from(mContext)

        manager.notify(123,builder.build())

    }

    private fun setList(jsonObject: JSONObject) {

        val cityList = Gson().fromJson(jsonObject.toString(), ModelCityList::class.java)
        val cityListAdapter= CityListAdapter(mContext, R.layout.single_list,object :CityListAdapter.OnCardClickListener{
            override fun onCardClick(singleCity: SingleCity) {

                if(PermissionUtils.checkPermission(mContext)){
                    if(PermissionUtils.isGpsEnable(mContext)){

                        val action= ListFragmentDirections.actionListFragmentToDetailsFragment(singleCity)
                        findNavController().navigate(action)
                    }
                    else{
                        PermissionUtils.enableGps(mContext)
                    }
                }

            }

        })
        binding.rvList.apply {
            setHasFixedSize(true)
            adapter= cityListAdapter
        }
        cityList.list?.let {
            cityListAdapter.addAll(it,true)
        }

    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(mContext)
            .registerReceiver(mMessageReceiver, IntentFilter(AppConstants.INTENT_FILTER))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMessageReceiver)
    }


    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val value= intent.extras?.getString(AppConstants.BROADCAST_KEY)
            Log.d("xxx", "onReceive: $value")
            getMyLocation()

        }
    }

}
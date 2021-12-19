package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.base.BaseFragment
import com.example.weatherapp.core.AppConstants
import com.example.weatherapp.databinding.FragmentSplashBinding
import com.example.weatherapp.networking.APIService
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.PermissionUtils
import com.example.weatherapp.viewmodel.network.WeatherViewModel

class SplashFragment : BaseFragment<WeatherViewModel, FragmentSplashBinding, WeatherRepository>() {

    override fun getViewModel() = WeatherViewModel::class.java
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentSplashBinding.inflate(inflater, container, false)
    override fun getRepository() = WeatherRepository(remoteDataSource.buildApi(APIService::class.java))

    companion object {
        fun newInstance(): SplashFragment {
            val args = Bundle()
            val fragment = SplashFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun init(savedInstanceState: Bundle?) {

        val handler= Handler()
        handler.postDelayed({

            checkPerm()

        },2000)

    }

    private fun checkPerm() {

        if(PermissionUtils.checkPermission(context)){

            val action= SplashFragmentDirections.actionSplashFragmentToListFragment()
            findNavController().navigate(action)

        }
        else{
            requestMultiplePermissions.launch(
                arrayOf(PermissionUtils.ACCESS_FINE_LOCATION,PermissionUtils.ACCESS_COARSE_LOCATION)
            )
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission->

        if(permission[PermissionUtils.ACCESS_FINE_LOCATION]==true && permission[PermissionUtils.ACCESS_COARSE_LOCATION] == true){
            Log.d("mymsg", "Request Check: Permission Granted")
            replaceScreen(ListFragment.newInstance(),AppConstants.FRAGMENT_LIST,true)
        }
        else{
            Log.d("mymsg", "Request Check: Permission Not Granted")
        }

    }

}


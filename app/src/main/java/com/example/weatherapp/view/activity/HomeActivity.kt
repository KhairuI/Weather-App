package com.example.weatherapp.view.activity

import android.os.Bundle
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseActivity
import com.example.weatherapp.core.AppConstants
import com.example.weatherapp.databinding.ActivityHomeBinding
import com.example.weatherapp.view.fragments.SplashFragment

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override val contentView: Int get() = R.layout.activity_home

    override fun init(savedInstanceState: Bundle?) {




       // replaceFragment(SplashFragment.newInstance(), AppConstants.FRAGMENT_SPLASH, false)

    }
}
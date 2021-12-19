package com.example.weatherapp.base

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.weatherapp.R

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val contentView: Int
    protected lateinit var binding: B

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setWindow() {
       /* window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, contentView)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setWindow()
        init(savedInstanceState)
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    fun invokeActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    fun invokeActivity(cls: Class<*>?, key: String, value: String) {
        val intent = Intent(this, cls).apply {
            flags = FLAG_ACTIVITY_CLEAR_TASK
            putExtra(key, value)
        }
        startActivity(intent)
    }

    fun invokeActivity(activity: Activity, cls: Class<*>?) {
        val intent = Intent(activity, cls)
        activity.startActivity(intent)
    }

    fun invokeActivityAndFinish(cls: Class<*>?) {
        val intent = Intent(this, cls).apply {
            flags = FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    protected fun replaceFragment(fragment: Fragment, tag: String, allowBackTrack: Boolean) {
        if (allowBackTrack) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.fragment_container, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

}
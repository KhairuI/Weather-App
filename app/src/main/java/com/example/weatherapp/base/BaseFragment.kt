package com.example.weatherapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.networking.RemoteDataSource
import com.example.weatherapp.viewmodel.factory.NetworkViewModelFactory

abstract class BaseFragment<VM : ViewModel, B : ViewDataBinding, R : BaseRepository> : Fragment() {

    // Fields ======================================================================================

    lateinit var viewModel: VM
    protected lateinit var binding: B
    protected val remoteDataSource = RemoteDataSource()
    protected lateinit var mContext: Context

    // ======================================================

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getBinding(inflater, container)
        val factory = NetworkViewModelFactory(getRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        binding.lifecycleOwner = this
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    abstract fun getViewModel(): Class<VM>

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getRepository(): R

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(mContext)
    }

    protected fun replaceScreen(fragment: Fragment, tag: String, allowBackTrack: Boolean) {
        if (allowBackTrack) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    com.example.weatherapp.R.anim.slide_in_left_animation,
                    com.example.weatherapp.R.anim.slide_out_right_animation,
                    com.example.weatherapp.R.anim.slide_in_right_animation,
                    com.example.weatherapp.R.anim.slide_out_left_animation
                )
                .replace(com.example.weatherapp.R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit()
        } else {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    com.example.weatherapp.R.anim.slide_in_left_animation,
                    com.example.weatherapp.R.anim.slide_out_right_animation,
                    com.example.weatherapp.R.anim.slide_in_right_animation,
                    com.example.weatherapp.R.anim.slide_out_left_animation
                )
                .disallowAddToBackStack()
                .replace(com.example.weatherapp.R.id.fragment_container, fragment, tag)
                .commit()
        }
    }


}
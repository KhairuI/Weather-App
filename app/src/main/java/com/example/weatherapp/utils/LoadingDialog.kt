package com.example.weatherapp.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.weatherapp.databinding.LayoutLoadingDialogBinding

class LoadingDialog(context: Context) : AlertDialog(context) {
    init {
        setView(LayoutLoadingDialogBinding.inflate(LayoutInflater.from(context), null, false).root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
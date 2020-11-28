package com.example.currencycalculator.util

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.currencycalculator.R
import com.example.currencycalculator.ui.convert.getImageUrl

fun Fragment.showMessage(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(name: String) {
    if (name.isNotEmpty()) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.default_logo)
            .error(R.drawable.default_logo)

        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)
            .load(getImageUrl(name))
            .into(this)
    }
}
package com.tal.android.pingpong.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.tal.android.pingpong.R
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation

fun ImageView?.load(url: String? = null, @DrawableRes fallbackImage: Int? = null, rounded: Boolean = false) {
    if (this == null) {
        return
    }
    if (url == null) {
        setImageResource(fallbackImage ?: R.drawable.ic_incognito)
    } else {
        var builder = Glide
            .with(context)
            .load(url)
            .error(R.drawable.ic_incognito)
        if (rounded) {
            builder = builder.transform(CropCircleWithBorderTransformation())
        }
        fallbackImage?.let {
            builder = builder.error(it)
        }
        builder.into(this)
    }
}
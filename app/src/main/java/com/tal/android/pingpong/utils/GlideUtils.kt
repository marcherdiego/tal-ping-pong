package com.tal.android.pingpong.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.tal.android.pingpong.R
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation

object GlideUtils {
    fun loadImage(url: String? = null, imageView: ImageView?, @DrawableRes fallbackImage: Int? = null, rounded: Boolean = false) {
        imageView ?: return
        if (url == null) {
            imageView.setImageResource(fallbackImage ?: R.drawable.ic_incognito)
        } else {
            var builder = Glide
                .with(imageView.context)
                .load(url)
                .error(R.drawable.ic_incognito)
            if (rounded) {
                builder = builder.transform(CropCircleWithBorderTransformation())
            }
            fallbackImage?.let {
                builder = builder.error(it)
            }
            builder.into(imageView)
        }
    }
}
package com.tal.android.pingpong.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tal.android.pingpong.R

object ColorUtils {
    @ColorInt
    fun getDifficultyColor(context: Context, level: Float) = ContextCompat.getColor(
        context,
        when (level) {
            in 0f..0.05f -> R.color.chances_0_5
            in 0.05f..0.10f -> R.color.chances_5_10
            in 0.10f..0.15f -> R.color.chances_10_15
            in 0.15f..0.20f -> R.color.chances_15_20
            in 0.20f..0.25f -> R.color.chances_20_25
            in 0.25f..0.30f -> R.color.chances_25_30
            in 0.30f..0.35f -> R.color.chances_30_35
            in 0.35f..0.40f -> R.color.chances_35_40
            in 0.40f..0.45f -> R.color.chances_40_45
            in 0.45f..0.50f -> R.color.chances_45_50
            in 0.50f..0.55f -> R.color.chances_50_55
            in 0.55f..0.60f -> R.color.chances_55_60
            in 0.60f..0.65f -> R.color.chances_60_65
            in 0.65f..0.70f -> R.color.chances_65_70
            in 0.70f..0.75f -> R.color.chances_70_75
            in 0.75f..0.80f -> R.color.chances_75_80
            in 0.80f..0.85f -> R.color.chances_80_85
            in 0.85f..0.90f -> R.color.chances_85_90
            in 0.90f..0.95f -> R.color.chances_90_95
            else -> R.color.chances_95_100
        }
    )
}
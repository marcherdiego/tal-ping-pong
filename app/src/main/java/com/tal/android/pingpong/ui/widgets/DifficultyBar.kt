package com.tal.android.pingpong.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.asPercentString
import kotlin.math.max
import kotlin.math.min

class DifficultyBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var winChancesLabel: TextView
    private var difficultyLevelGuideline: Guideline

    init {
        addView(
            LayoutInflater
                .from(context)
                .inflate(R.layout.difficulty_bar, null)
        )
        winChancesLabel = findViewById(R.id.win_chances_label)
        difficultyLevelGuideline = findViewById(R.id.guideline)
    }

    fun setup(local: User, visitor: User? = null, localCompanion: User? = null, visitorCompanion: User? = null) {
        val winChances = chancesToWin(local, visitor, localCompanion, visitorCompanion)
        if (winChances == User.UNKNOWN) {
            difficultyLevelGuideline.setGuidelinePercent(User.CHANCES_HALF)
            winChancesLabel.text = context.getString(R.string.unknown)
        } else {
            difficultyLevelGuideline.setGuidelinePercent(winChances)
            winChancesLabel.text = (100 * winChances).asPercentString(digits = 0)
        }
    }

    private fun chancesToWin(local: User, visitor: User? = null, localCompanion: User? = null, visitorCompanion: User? = null): Float {
        val visitor = visitor ?: return User.UNKNOWN
        val localRatio = local.matchesRatioValue
        val visitorRatio = visitor.matchesRatioValue
        val ratio: Float
        var ratioSum = localRatio + visitorRatio
        if (localCompanion == null && visitorCompanion == null) {
            if (localRatio * visitorRatio == 0f) {
                // If any of the two is zero, then there is not enough data to process
                return User.UNKNOWN
            }
            ratio = visitor.matchesRatioValue / ratioSum
        } else {
            val localCompanionRatio = localCompanion?.matchesRatioValue ?: return User.UNKNOWN
            val visitorCompanionRatio = visitorCompanion?.matchesRatioValue ?: return User.UNKNOWN
            if (localCompanionRatio * visitorCompanionRatio == 0f) {
                // If any of the two is zero, then there is not enough data to process
                return User.UNKNOWN
            }
            ratioSum += localCompanionRatio + visitorCompanionRatio
            ratio = (visitorRatio + visitorCompanionRatio) / ratioSum
        }

        // Limit value to CHANCES_LOWER_BOUND% and CHANCES_UPPER_BOUND%
        return max(User.CHANCES_LOWER_BOUND, min(ratio, User.CHANCES_UPPER_BOUND))
    }
}
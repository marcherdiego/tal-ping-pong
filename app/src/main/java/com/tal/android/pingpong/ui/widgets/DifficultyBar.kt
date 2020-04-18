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
    private var title: TextView
    private var winChancesLabel: TextView
    private var difficultyLevelGuideline: Guideline

    init {
        addView(
            LayoutInflater
                .from(context)
                .inflate(R.layout.difficulty_bar, null)
        )
        title = findViewById(R.id.win_chances_title)
        winChancesLabel = findViewById(R.id.win_chances_label)
        difficultyLevelGuideline = findViewById(R.id.guideline)
    }

    fun setup(localRatio: Float, visitorRatio: Float? = null, localCompanionRatio: Float? = null, visitorCompanionRatio: Float? = null) {
        val winChances = chancesToWin(localRatio, visitorRatio, localCompanionRatio, visitorCompanionRatio)
        if (winChances == User.UNKNOWN) {
            difficultyLevelGuideline.setGuidelinePercent(User.CHANCES_HALF)
            winChancesLabel.text = context.getString(R.string.unknown)
        } else {
            difficultyLevelGuideline.setGuidelinePercent(winChances)
            winChancesLabel.text = (100 * winChances).asPercentString(digits = 0)
        }
    }

    fun setupForMe(successRate: Float) {
        if (successRate == User.UNKNOWN) {
            difficultyLevelGuideline.setGuidelinePercent(User.CHANCES_HALF)
            winChancesLabel.text = context.getString(R.string.unknown)
        } else {
            difficultyLevelGuideline.setGuidelinePercent(successRate)
            winChancesLabel.text = (100 * successRate).asPercentString(digits = 0)
        }
    }

    fun setTitle(title: String?) {
        this.title.text = title
    }

    private fun chancesToWin(localRatio: Float, visitorRatio: Float? = null,
                             localCompanionRatio: Float? = null, visitorCompanionRatio: Float? = null): Float {
        val visitorRatio = visitorRatio ?: return User.UNKNOWN
        val localUsersRatio: Float
        val visitorLoseRatio: Float
        if (localCompanionRatio == null && visitorCompanionRatio == null) {
            //Singles match
            if (localRatio * visitorRatio == 0f) {
                // If any of the two is zero, then there is not enough data to process
                return User.UNKNOWN
            }
            localUsersRatio = localRatio
            visitorLoseRatio = 1 - visitorRatio
        } else {
            //Doubles match
            val localCompanionRatio = localCompanionRatio ?: return User.UNKNOWN
            val visitorCompanionRatio = visitorCompanionRatio ?: return User.UNKNOWN
            if (localCompanionRatio * visitorCompanionRatio == 0f) {
                // If any of the two is zero, then there is not enough data to process
                return User.UNKNOWN
            }
            localUsersRatio = (localCompanionRatio + localRatio) / 2f
            visitorLoseRatio = 1 - ((visitorCompanionRatio + visitorRatio) / 2f)
        }
        val ratio = (localUsersRatio + visitorLoseRatio) / 2f

        // Limit value to CHANCES_LOWER_BOUND% and CHANCES_UPPER_BOUND%
        return max(User.CHANCES_LOWER_BOUND, min(ratio, User.CHANCES_UPPER_BOUND))
    }
}
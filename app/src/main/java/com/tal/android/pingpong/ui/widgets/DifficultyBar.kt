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

    fun setup(localUser: User, visitorUser: User) {
        val winChances = visitorUser.chancesToWin(localUser)
        if (winChances == User.UNKNOWN) {
            difficultyLevelGuideline.setGuidelinePercent(User.CHANCES_HALF)
            winChancesLabel.text = context.getString(R.string.unknown)
        } else {
            difficultyLevelGuideline.setGuidelinePercent(winChances)
            winChancesLabel.text = (100 * winChances).asPercentString(digits = 0)
        }
    }
}
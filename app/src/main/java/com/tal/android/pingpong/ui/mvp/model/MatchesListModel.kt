package com.tal.android.pingpong.ui.mvp.model

import androidx.annotation.StringDef
import com.tal.android.pingpong.domain.MatchRecord

class MatchesListModel(
    val match: MatchRecord?,
    val selectedTab: String,
    val actionType: String
) : BaseModel() {
    companion object {
        /**
         * Possible tabs states
         */
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(TabsState.UPCOMING, TabsState.UNCONFIRMED, TabsState.PAST)
        annotation class TabsState {
            companion object {
                const val UPCOMING = "upcoming"
                const val UNCONFIRMED = "invitations"
                const val PAST = "past"
            }
        }
    }
}

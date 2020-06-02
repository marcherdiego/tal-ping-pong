package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.MultiSelectUsersListAdapter

class NewChampionshipView(activity: BaseActivity<*>) : BaseActivityView(activity) {

    private val toolbar: Toolbar = activity.findViewById(R.id.toolbar)

    private val title: TextView = activity.findViewById(R.id.title)
    private val date: TextView = activity.findViewById(R.id.date)
    private val usersSelected: TextView = activity.findViewById(R.id.users_selected)
    private val usersList: RecyclerView = activity.findViewById(R.id.users_list)

    init {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activity.findViewById<View>(R.id.edit_title_button).setOnClickListener {
            bus.post(TitleEditButtonClickedEvent())
        }
        activity.findViewById<View>(R.id.edit_date_button).setOnClickListener {
            bus.post(DateEditButtonClickedEvent())
        }
    }
    fun setUsersAdapter(adapter: MultiSelectUsersListAdapter) {
        usersList.adapter = adapter
    }

    class TitleEditButtonClickedEvent
    class DateEditButtonClickedEvent
}

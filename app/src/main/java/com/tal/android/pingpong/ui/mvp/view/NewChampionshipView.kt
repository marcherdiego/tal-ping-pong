package com.tal.android.pingpong.ui.mvp.view

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.MultiSelectUsersListAdapter
import com.tal.android.pingpong.utils.load

class NewChampionshipView(activity: BaseActivity<*>) : BaseActivityView(activity) {

    private val toolbar: Toolbar = activity.findViewById(R.id.toolbar)

    private val icon: ImageView = activity.findViewById(R.id.icon)
    private val title: TextView = activity.findViewById(R.id.title)
    private val date: TextView = activity.findViewById(R.id.date)
    private val usersSelected: TextView = activity.findViewById(R.id.users_selected)
    private val usersList: RecyclerView = activity.findViewById(R.id.users_list)

    init {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onClick(R.id.icon, IconEditButtonClickedEvent())
        onClick(R.id.edit_icon_button, IconEditButtonClickedEvent())

        onClick(R.id.title, TitleEditButtonClickedEvent())
        onClick(R.id.edit_title_button, TitleEditButtonClickedEvent())

        onClick(R.id.date, DateEditButtonClickedEvent())
        onClick(R.id.edit_date_button, DateEditButtonClickedEvent())
    }

    fun setUsersAdapter(adapter: MultiSelectUsersListAdapter) {
        usersList.adapter = adapter
    }

    fun updateSelectedUsersCount(selectedUsers: Int) {
        usersSelected.text = activity?.resources?.getQuantityString(R.plurals.x_users_selected, selectedUsers, selectedUsers)
    }

    fun setChampionshipImage(image: String?) {
        icon.load(image, R.drawable.ic_championship)
    }

    fun setChampionshipName(name: String?) {
        title.text = name
    }

    fun setChampionshipDate(championshipDate: String?) {
        date.text = championshipDate
    }

    class IconEditButtonClickedEvent
    class TitleEditButtonClickedEvent
    class DateEditButtonClickedEvent
}

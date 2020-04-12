package com.tal.android.pingpong.ui.mvp.view

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nerdscorner.mvplib.events.activity.BaseActivity
import com.nerdscorner.mvplib.events.view.BaseActivityView
import com.tal.android.pingpong.R

class MainView(activity: BaseActivity<*>) : BaseActivityView(activity) {
    private val navigation: BottomNavigationView = activity.findViewById(R.id.bottom_navigation)
    private val appBarLayout: AppBarLayout = activity.findViewById(R.id.app_bar_layout)
    private val collapsingToolbar: CollapsingToolbarLayout = activity.findViewById(R.id.collapsing_toolbar)
    private val toolbar: Toolbar = activity.findViewById(R.id.toolbar)
    private val userImage: View = activity.findViewById(R.id.user_image)
    private val overlay: View = activity.findViewById(R.id.overlay)

    init {
        navigation.setOnNavigationItemSelectedListener {
            bus.post(NavigationItemSelectedEvent(it.itemId))
            true
        }
        activity.setSupportActionBar(toolbar)
    }

    fun setSelectedTab(@IdRes itemId: Int) {
        navigation.selectedItemId = itemId
    }

    fun setToolbarTitle(title: String) {
        toolbar.title = title
        collapsingToolbar.title = title
    }

    fun expandToolbar() {
        appBarLayout.setExpanded(true, false)
        userImage.visibility = View.VISIBLE
        overlay.visibility = View.VISIBLE
        collapsingToolbar.isTitleEnabled = true
        toolbar.background = null
    }

    fun collapseToolbar() {
        appBarLayout.setExpanded(false, false)
        userImage.visibility = View.GONE
        overlay.visibility = View.GONE
        collapsingToolbar.isTitleEnabled = false
        toolbar.setBackgroundColor(ContextCompat.getColor(activity ?: return, R.color.colorPrimary))
    }

    class NavigationItemSelectedEvent(val itemId: Int, val manualEvent: Boolean = false)
}

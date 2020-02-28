package com.tal.android.talpingpong.ui.mvp.presenter

import androidx.fragment.app.Fragment
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.ui.fragments.ChallengesFragment
import com.tal.android.talpingpong.ui.fragments.ProfileFragment
import com.tal.android.talpingpong.ui.fragments.UsersListFragment

import com.tal.android.talpingpong.ui.mvp.model.MainModel
import com.tal.android.talpingpong.ui.mvp.view.MainView
import org.greenrobot.eventbus.Subscribe

class MainPresenter(view: MainView, model: MainModel) :
    BaseActivityPresenter<MainView, MainModel>(view, model) {

    @Subscribe
    fun onNavigationItemSelected(event: MainView.NavigationItemSelectedEvent) {
        val currentState = model.currentState
        val (newState, fragment) = when (event.itemId) {
            R.id.menu_challenges -> Pair(MainModel.CHALLENGES, ChallengesFragment())
            R.id.menu_new_match -> Pair(MainModel.FIND_RIVAL, UsersListFragment())
            R.id.menu_profile -> Pair(MainModel.PROFILE, ProfileFragment())
            else -> return
        }
        model.currentState = newState
        if (currentState == model.currentState) {
            //If re-selecting tab, ignore event
            return
        }
        updateCurrentFragment(fragment)
    }

    private fun updateCurrentFragment(fragment: Fragment) {
        view.withFragmentTransaction {
            if (!fragment.isAdded) {
                replace(R.id.frame_container, fragment)
            }
            commitNow()
        }
    }
}

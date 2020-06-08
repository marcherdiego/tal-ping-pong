package com.tal.android.pingpong.ui.mvp.view

import android.animation.Animator
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nerdscorner.mvplib.events.view.BaseFragmentView
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.adapters.recyclerview.UsersListAdapter
import com.tal.android.pingpong.ui.widgets.listeners.SimpleAnimatorListener
import com.tal.android.pingpong.utils.onTextChanged

class UsersListView(fragment: Fragment) : BaseFragmentView(fragment) {
    private val coordinatorLayout: CoordinatorLayout? = fragment.view?.findViewById(R.id.coordinator_layout)
    private val usersList: RecyclerView? = fragment.view?.findViewById(R.id.users_list)
    private val refreshLayout: SwipeRefreshLayout? = activity?.findViewById(R.id.refresh_layout)
    private val filterCriteria: EditText? = fragment.view?.findViewById(R.id.filter_criteria)

    private val newChampionshipContainer: LinearLayout? = fragment.view?.findViewById(R.id.new_championship_container)
    private val newChampionshipButtonLabel: TextView? = fragment.view?.findViewById(R.id.new_championship_button_label)
    private val newChampionshipButton: FloatingActionButton? = fragment.view?.findViewById(R.id.new_championship_button)

    private val newDoublesMatchContainer: LinearLayout? = fragment.view?.findViewById(R.id.new_doubles_match_container)
    private val newDoublesMatchButtonLabel: TextView? = fragment.view?.findViewById(R.id.new_doubles_match_button_label)
    private val newDoublesMatchButton: FloatingActionButton? = fragment.view?.findViewById(R.id.new_doubles_match_button)

    private val newSinglesMatchContainer: LinearLayout? = fragment.view?.findViewById(R.id.new_singles_match_container)
    private val newSinglesMatchButtonLabel: TextView? = fragment.view?.findViewById(R.id.new_singles_match_button_label)
    private val newSinglesMatchButton: FloatingActionButton? = fragment.view?.findViewById(R.id.new_singles_match_button)

    private val overlay: View? = fragment.view?.findViewById(R.id.overlay)

    private val newButton: FloatingActionButton? = fragment.view?.findViewById(R.id.new_button)

    init {
        filterCriteria?.onTextChanged {
            bus.post(SearchCriteriaChangedEvent(it?.toString()))
        }
        usersList?.addItemDecoration(
            DividerItemDecoration(fragment.context, DividerItemDecoration.VERTICAL)
        )
        refreshLayout?.setOnRefreshListener {
            bus.post(RefreshUsersListsEvent())
        }
        newChampionshipButton?.setOnClickListener {
            bus.post(NewChampionshipButtonClickedEvent())
        }
        newDoublesMatchButton?.setOnClickListener {
            bus.post(NewDoublesMatchButtonClickedEvent())
        }
        newSinglesMatchButton?.setOnClickListener {
            bus.post(NewSinglesMatchButtonClickedEvent())
        }
        newButton?.setOnClickListener {
            bus.post(NewButtonClickedEvent())
        }
        overlay?.setOnClickListener {
            bus.post(OverlayClickedEvent())
        }
        overlay?.visibility = View.GONE
        overlay?.alpha = 0F

        newChampionshipButtonLabel?.alpha = 0F
        newDoublesMatchButtonLabel?.alpha = 0F
        newSinglesMatchButtonLabel?.alpha = 0F
    }

    fun setUsersListAdapter(adapter: UsersListAdapter) {
        usersList?.adapter = adapter
    }

    fun setRefreshing(refreshing: Boolean) {
        refreshLayout?.isRefreshing = refreshing
    }

    fun expandFabOptions() {
        withActivity {
            newButton?.animate()?.rotation(resources.getInteger(R.integer.degrees_45).toFloat())

            newChampionshipContainer?.animate()?.translationY(-resources.getDimension(R.dimen.standard_155))
            newChampionshipButtonLabel?.animate()?.alpha(1F)

            newDoublesMatchContainer?.animate()?.translationY(-resources.getDimension(R.dimen.standard_105))
            newDoublesMatchButtonLabel?.animate()?.alpha(1F)

            newSinglesMatchContainer?.animate()?.translationY(-resources.getDimension(R.dimen.standard_55))
            newSinglesMatchButtonLabel?.animate()?.alpha(1F)

            overlay?.visibility = View.VISIBLE
            overlay?.animate()?.alpha(1F)
        }
    }

    fun collapseFabOptions() {
        withActivity {
            newButton?.animate()?.rotation(0F)

            newChampionshipContainer?.animate()?.translationY(0F)
            newChampionshipButtonLabel?.animate()?.alpha(0F)

            newDoublesMatchContainer?.animate()?.translationY(0F)
            newDoublesMatchButtonLabel?.animate()?.alpha(0F)

            newSinglesMatchContainer?.animate()?.translationY(0F)
            newSinglesMatchButtonLabel?.animate()?.alpha(0F)

            overlay?.animate()?.alpha(0F)?.setListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    overlay.animate().setListener(null)
                    overlay.visibility = View.GONE
                }
            })
        }
    }

    fun showSnackbar(text: Int) {
        Snackbar.make(coordinatorLayout ?: return, text, Snackbar.LENGTH_SHORT).show()
    }

    fun clearSearchBox() {
        filterCriteria?.text = null
    }

    fun filterUsers(criteria: String) {
        (usersList?.adapter as? UsersListAdapter)?.filter(criteria)
    }

    class SearchCriteriaChangedEvent(val criteria: String?)
    class RefreshUsersListsEvent
    class NewChampionshipButtonClickedEvent
    class NewDoublesMatchButtonClickedEvent
    class NewSinglesMatchButtonClickedEvent
    class NewButtonClickedEvent

    class OverlayClickedEvent
}

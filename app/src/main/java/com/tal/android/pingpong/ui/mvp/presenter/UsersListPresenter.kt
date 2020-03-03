package com.tal.android.pingpong.ui.mvp.presenter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.nerdscorner.mvplib.events.presenter.BaseFragmentPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidChallengeTimeException
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.ui.mvp.model.UsersListModel
import com.tal.android.pingpong.ui.mvp.view.UsersListView
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import org.greenrobot.eventbus.Subscribe
import java.util.*

class UsersListPresenter(view: UsersListView, model: UsersListModel) :
    BaseFragmentPresenter<UsersListView, UsersListModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: UsersListModel.UsersFetchedSuccessfullyEvent) {
        view.setUsersListAdapter(UsersListAdapter(event.usersList, model.getBus()))
        view.setRefreshing(false)
    }

    @Subscribe
    fun onUsersFetchFailed(event: UsersListModel.UsersFetchFailedEvent) {
        view.showToast(R.string.failed_to_load_users_list)
        view.setRefreshing(false)
    }

    @Subscribe
    fun onRefreshLists(event: UsersListView.RefreshUsersListsEvent) {
        model.fetchUsers()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        view.withActivity {
            val challengeDialogView = LayoutInflater
                .from(this)
                .inflate(R.layout.challenges_user_dialog, null)
            val userImage = challengeDialogView.findViewById<ImageView>(R.id.user_image)
            val userName = challengeDialogView.findViewById<TextView>(R.id.user_name)
            val userEmail = challengeDialogView.findViewById<TextView>(R.id.user_email)
            val userStats = challengeDialogView.findViewById<TextView>(R.id.user_stats)

            val user = event.user
            GlideUtils.loadImage(user.userImage, userImage, R.drawable.ic_incognito, true)
            userName.text = user.userName
            userEmail.text = user.userEmail
            userStats.text = getString(R.string.user_stats, user.matchesWon, user.matchesLost, user.matchesRatio)

            DialogFactory
                .newBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.challenge_user)
                .setView(challengeDialogView)
                .setPositiveButtonText(R.string.challenge)
                .setNegativeButtonText(R.string.cancel)
                .setPositiveButtonListener {
                    openChallengeDateSelectionDialog(user)
                }
                .build()
                .show()
        }
    }

    private fun openChallengeDateSelectionDialog(user: User) {
        view.withActivity {
            val today = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    openChallengeTimeSelectionDialog(user, year, monthOfYear, dayOfMonth)
                },
                today[Calendar.YEAR],
                today[Calendar.MONTH],
                today[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.datePicker.minDate = today.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun openChallengeTimeSelectionDialog(user: User, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        view.withActivity {
            val now = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                OnTimeSetListener { _, hourOfDay, minute ->
                    try {
                        model.challengeUser(user, year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    } catch (e: InvalidChallengeTimeException) {
                        view.showToast(R.string.invalid_time_in_the_past)
                        openChallengeTimeSelectionDialog(user, year, monthOfYear, dayOfMonth)
                    }
                },
                now[Calendar.HOUR_OF_DAY],
                now[Calendar.MINUTE],
                false
            )
            timePickerDialog.show()
        }
    }

    override fun onResume() {
        view.setRefreshing(true)
        model.fetchUsers()
    }
}

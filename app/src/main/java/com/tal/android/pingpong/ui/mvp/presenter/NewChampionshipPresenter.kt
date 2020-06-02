package com.tal.android.pingpong.ui.mvp.presenter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.MenuItem
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.adapters.MultiSelectUsersListAdapter

import com.tal.android.pingpong.ui.mvp.model.NewChampionshipModel
import com.tal.android.pingpong.ui.mvp.view.NewChampionshipView
import org.greenrobot.eventbus.Subscribe
import java.util.*

class NewChampionshipPresenter(view: NewChampionshipView, model: NewChampionshipModel) :
    BaseActivityPresenter<NewChampionshipView, NewChampionshipModel>(view, model) {

    @Subscribe
    fun onUsersFetchedSuccessfully(event: NewChampionshipModel.UsersFetchedSuccessfullyEvent) {
        view.setUsersAdapter(MultiSelectUsersListAdapter(model.usersList, model.getBus(), model.selectedUsers))
    }

    @Subscribe
    fun onUsersFetchFailed(event: NewChampionshipModel.UsersFetchFailedEvent) {
        view.showToast(R.string.network_error_message)
        view.activity?.finish()
    }

    @Subscribe
    fun onTitleEditButtonClicked(event: NewChampionshipView.TitleEditButtonClickedEvent) {

    }

    @Subscribe
    fun onDateEditButtonClicked(event: NewChampionshipView.DateEditButtonClickedEvent) {
        openDateSelectionDialog(view.activity ?: return)
    }

    private fun openDateSelectionDialog(context: Context) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                openTimeSelectionDialog(context, year, monthOfYear, dayOfMonth)
            },
            today[Calendar.YEAR],
            today[Calendar.MONTH],
            today[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun openTimeSelectionDialog(context: Context,year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                try {
                    model.saveChampionshipDate(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                } catch (e: InvalidMatchTimeException) {
                    openTimeSelectionDialog(context, year, monthOfYear, dayOfMonth)
                }
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            view.activity?.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        model.fetchUsers()
    }
}

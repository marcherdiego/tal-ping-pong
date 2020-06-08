package com.tal.android.pingpong.ui.mvp.presenter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.nerdscorner.mvplib.events.presenter.BaseActivityPresenter
import com.tal.android.pingpong.R
import com.tal.android.pingpong.exceptions.InvalidChampionshipNameException
import com.tal.android.pingpong.exceptions.InvalidChampionshipTimeException
import com.tal.android.pingpong.exceptions.InvalidChampionshipUsersListException
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.adapters.recyclerview.MultiSelectUsersListAdapter
import com.tal.android.pingpong.ui.dialogs.TextInputDialog

import com.tal.android.pingpong.ui.mvp.model.NewChampionshipModel
import com.tal.android.pingpong.ui.mvp.view.NewChampionshipView
import com.tal.android.pingpong.utils.DateUtils
import org.greenrobot.eventbus.Subscribe
import java.util.*

class NewChampionshipPresenter(view: NewChampionshipView, model: NewChampionshipModel) :
    BaseActivityPresenter<NewChampionshipView, NewChampionshipModel>(view, model) {

    init {
        view.updateSelectedUsersCount(1)
        view.setChampionshipImage(null)
    }

    @Subscribe
    fun onUsersFetchedSuccessfully(event: NewChampionshipModel.UsersFetchedSuccessfullyEvent) {
        view.setUsersAdapter(
            MultiSelectUsersListAdapter(
                model.usersList,
                model.getCurrentUser() ?: return,
                model.getBus(),
                model.selectedUsers
            )
        )
    }

    @Subscribe
    fun onUsersFetchFailed(event: NewChampionshipModel.UsersFetchFailedEvent) {
        view.showToast(R.string.network_error_message)
        view.activity?.finish()
    }

    @Subscribe
    fun onIconEditButtonClicked(event: NewChampionshipView.IconEditButtonClickedEvent) {
        // Open file chooser
    }

    @Subscribe
    fun onTitleEditButtonClicked(event: NewChampionshipView.TitleEditButtonClickedEvent) {
        val championshipName = model.championshipName
        TextInputDialog(model.getBus()).show(
            context = view.activity ?: return,
            title = R.string.championship_name,
            positiveButtonText = R.string.accept,
            negativeButtonText = R.string.cancel,
            hint = R.string.championship_name,
            preloadedInput = championshipName
        )
    }

    @Subscribe
    fun onPositiveButtonClicked(event: TextInputDialog.PositiveButtonClickedEvent) {
        model.championshipName = event.input
        view.setChampionshipName(model.championshipName)
    }

    @Subscribe
    fun onDateEditButtonClicked(event: NewChampionshipView.DateEditButtonClickedEvent) {
        openDateSelectionDialog(view.activity ?: return)
    }

    @Subscribe
    fun onSelectedUsersChanged(event: MultiSelectUsersListAdapter.SelectedUsersChangedEvent) {
        view.updateSelectedUsersCount(event.selectedUsers.size)
    }

    @Subscribe
    fun onChampionshipCreatedSuccessfully(event: NewChampionshipModel.ChampionshipCreatedSuccessfullyEvent) {
        view.showToast(R.string.championship_created)
        view.activity?.finish()
    }

    @Subscribe
    fun onChampionshipCreationFailed(event: NewChampionshipModel.ChampionshipCreationFailedEvent) {
        view.showToast(R.string.championship_creation_failed)
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

    private fun openTimeSelectionDialog(context: Context, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                try {
                    model.saveChampionshipDate(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    val formattedDate = DateUtils.formatDate(model.championshipDate)
                    view.setChampionshipDate(formattedDate)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        view.activity?.menuInflater?.inflate(R.menu.new_championship_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                view.activity?.finish()
                true
            }
            R.id.menu_save -> {
                try {
                    model.createChampionship()
                } catch (e: InvalidChampionshipTimeException) {
                    view.showToast(R.string.invalid_championship_time)
                } catch (e: InvalidChampionshipNameException) {
                    view.showToast(R.string.invalid_championship_name)
                } catch (e: InvalidChampionshipUsersListException) {
                    view.showToast(R.string.invalid_championship_users_list)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        model.fetchUsers()
    }
}

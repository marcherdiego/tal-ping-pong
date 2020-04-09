package com.tal.android.pingpong.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.exceptions.InvalidMatchTimeException
import com.tal.android.pingpong.ui.widgets.DifficultyBar
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.GlideUtils
import org.greenrobot.eventbus.Subscribe
import java.util.*

class NewDoublesMatchDialog(private val users: List<User>, private val myUser: User, private val bus: Bus) {

    private var dialog: AlertDialog? = null
    private val userSelectorBus = Bus.newInstance

    private lateinit var localImage: ImageView
    private lateinit var localCompanionImage: ImageView
    private lateinit var visitorImage: ImageView
    private lateinit var visitorCompanionImage: ImageView

    private var localCompanion: User? = null
    private var visitor: User? = null
    private var visitorCompanion: User? = null

    fun show(context: Context) {
        userSelectorBus.register(this)
        val challengeDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.doubles_challenge_proposal_dialog, null)
        localImage = challengeDialogView.findViewById(R.id.local_1_image)
        localCompanionImage = challengeDialogView.findViewById(R.id.local_2_image)
        visitorImage = challengeDialogView.findViewById(R.id.visitor_1_image)
        visitorCompanionImage = challengeDialogView.findViewById(R.id.visitor_2_image)

        val difficultyBar: DifficultyBar = challengeDialogView.findViewById(R.id.difficulty_bar)
        difficultyBar.setup(local = myUser)

        GlideUtils.loadImage(myUser.userImage, localImage, R.drawable.ic_incognito, true)
        GlideUtils.loadImage(imageView = localCompanionImage, fallbackImage = R.drawable.ic_incognito)
        GlideUtils.loadImage(imageView = visitorImage, fallbackImage = R.drawable.ic_incognito)
        GlideUtils.loadImage(imageView = visitorCompanionImage, fallbackImage = R.drawable.ic_incognito)

        localCompanionImage.setOnClickListener {
            UserSelectorDialog(users, userSelectorBus, UserSelectorDialog.LOCAL_COMPANION).show(it.context)
        }
        visitorImage.setOnClickListener {
            UserSelectorDialog(users, userSelectorBus, UserSelectorDialog.VISITOR).show(it.context)
        }
        visitorCompanionImage.setOnClickListener {
            UserSelectorDialog(users, userSelectorBus, UserSelectorDialog.VISITOR_COMPANION).show(it.context)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.user_details)
            .setView(challengeDialogView)
            .setPositiveButtonText(R.string.challenge)
            .setPositiveButtonListener {
                openDateSelectionDialog(context, MatchRecord())
            }
            .setNegativeButtonText(R.string.close)
            .setNegativeButtonListener {
                dialog?.dismiss()
            }
            .setOnDismissListener(DialogInterface.OnDismissListener {
                userSelectorBus.unregister(this)
            })
            .build(context)
        dialog?.show()
    }

    @Subscribe
    fun onUserSelected(event: UserSelectorDialog.UserSelectedEvent) {
        val userImageView = when (event.userType) {
            UserSelectorDialog.LOCAL_COMPANION -> {
                localCompanion = event.user
                localCompanionImage
            }
            UserSelectorDialog.VISITOR -> {
                visitor = event.user
                visitorImage
            }
            UserSelectorDialog.VISITOR_COMPANION -> {
                visitorCompanion = event.user
                visitorCompanionImage
            }
            else -> return
        }
        GlideUtils.loadImage(
            imageView = userImageView,
            url = event.user.userImage,
            fallbackImage = R.drawable.ic_incognito
        )
    }

    private fun openDateSelectionDialog(context: Context, match: MatchRecord) {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                openTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
            },
            today[Calendar.YEAR],
            today[Calendar.MONTH],
            today[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun openTimeSelectionDialog(context: Context, match: MatchRecord, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                try {
                    val matchDate = getMatchDate(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    bus.post(CreateNewDoublesMatchButtonClickedEvent(match, matchDate))
                } catch (e: InvalidMatchTimeException) {
                    bus.post(NewDoublesMatchInvalidTimeSelectedEvent())
                    openTimeSelectionDialog(context, match, year, monthOfYear, dayOfMonth)
                }
            },
            now[Calendar.HOUR_OF_DAY],
            now[Calendar.MINUTE],
            false
        )
        timePickerDialog.show()
    }

    @Throws(InvalidMatchTimeException::class)
    private fun getMatchDate(year: Int, monthOfYear: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int): Date {
        val now = Calendar.getInstance()
        val selectedDateTime = Calendar.getInstance()
        selectedDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
        if (selectedDateTime.before(now)) {
            throw InvalidMatchTimeException()
        }
        return selectedDateTime.time
    }

    class CreateNewDoublesMatchButtonClickedEvent(val match: MatchRecord, val matchDate: Date)
    class NewDoublesMatchInvalidTimeSelectedEvent
}
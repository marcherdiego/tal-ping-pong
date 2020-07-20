package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load
import com.tal.android.pingpong.utils.multiLet
import org.greenrobot.eventbus.Subscribe

class NewChampionshipDoubleMatchDialog(users: List<User>, myUser: User, private val myTeamMate: User, bus: Bus)
    : BaseChampionshipMatchDialog(users, myUser, bus) {

    private lateinit var visitorImage: ImageView
    private lateinit var visitorCompanionImage: ImageView

    private lateinit var localScore: EditText
    private lateinit var visitorScore: EditText

    private var visitor: User? = null
    private var visitorCompanion: User? = null

    override fun show(context: Context) {
        val userSelectorBus = Bus.newInstance
        userSelectorBus.register(this)
        val newMatchDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_championship_doubles_match_dialog, null)
        val localImage = newMatchDialogView.findViewById<ImageView>(R.id.local_image)
        val localCompanionImage = newMatchDialogView.findViewById<ImageView>(R.id.local_companion_image)
        visitorImage = newMatchDialogView.findViewById(R.id.visitor_image)
        visitorCompanionImage = newMatchDialogView.findViewById(R.id.visitor_companion_image)

        localScore = newMatchDialogView.findViewById(R.id.local_score)
        visitorScore = newMatchDialogView.findViewById(R.id.visitor_score)

        localImage.load(myUser.userImage, R.drawable.ic_incognito, true)
        localCompanionImage.load(myTeamMate.userImage, R.drawable.ic_incognito, true)
        visitorImage.load(fallbackImage = R.drawable.ic_incognito)
        visitorCompanionImage.load(fallbackImage = R.drawable.ic_incognito)

        visitorImage.setOnClickListener {
            userSelectorDialog = UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR, visitor)
            userSelectorDialog?.show(it.context)
        }
        visitorCompanionImage.setOnClickListener {
            userSelectorDialog = UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR_COMPANION, visitorCompanion)
            userSelectorDialog?.show(it.context)
        }

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.new_match_record)
            .setView(newMatchDialogView)
            .setPositiveButtonText(R.string.add)
            .setPositiveButtonListener {
                validateMatchMembersList()
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

    private fun validateMatchMembersList() {
        dialog?.context?.let { context ->
            multiLet(visitor, visitorCompanion) { visitor, visitorCompanion ->
                try {
                    openDateSelectionDialog(
                        context,
                        MatchRecord(
                            local = myUser,
                            localCompanion = myTeamMate,
                            visitor = visitor,
                            visitorCompanion = visitorCompanion,
                            localScore = localScore.text.toString().toInt(),
                            visitorScore = visitorScore.text.toString().toInt()
                        )
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, R.string.invalid_score, Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, R.string.some_participants_are_missing, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Subscribe
    fun onUserSelected(event: UserSelectorDialog.UserSelectedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                visitor = event.user
                visitorImage.load(url = event.user.userImage, rounded = true, fallbackImage = R.drawable.ic_incognito)
            }
            UserSelectorDialog.VISITOR_COMPANION -> {
                visitorCompanion = event.user
                visitorCompanionImage.load(url = event.user.userImage, rounded = true, fallbackImage = R.drawable.ic_incognito)
            }
            else -> return
        }
        selectedUsers.add(event.user)
    }

    @Subscribe
    fun onUserRemoved(event: UserSelectorDialog.UserRemovedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                selectedUsers.remove(visitor)
                visitor = null
                visitorImage.load(fallbackImage = R.drawable.ic_incognito)
            }
            UserSelectorDialog.VISITOR_COMPANION -> {
                selectedUsers.remove(visitorCompanion)
                visitorCompanion = null
                visitorCompanionImage.load(fallbackImage = R.drawable.ic_incognito)
            }
            else -> return
        }
    }
}

package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.MatchRecord
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.utils.DialogFactory
import com.tal.android.pingpong.utils.load
import org.greenrobot.eventbus.Subscribe

class NewChampionshipSinglesMatchDialog(users: List<User>, myUser: User, bus: Bus) : BaseChampionshipMatchDialog(users, myUser, bus) {

    private lateinit var localImage: ImageView
    private lateinit var localUserName: TextView

    private lateinit var visitorImage: ImageView
    private lateinit var visitorUserName: TextView

    private lateinit var localScore: EditText
    private lateinit var visitorScore: EditText

    private var visitor: User? = null

    override fun show(context: Context) {
        val userSelectorBus = Bus.newInstance
        userSelectorBus.register(this)
        val newMatchDialogView = LayoutInflater
            .from(context)
            .inflate(R.layout.new_championship_singles_match_dialog, null)
        localImage = newMatchDialogView.findViewById(R.id.local_image)
        localUserName = newMatchDialogView.findViewById(R.id.local_user_name)
        visitorImage = newMatchDialogView.findViewById(R.id.visitor_image)
        visitorUserName = newMatchDialogView.findViewById(R.id.visitor_user_name)

        localScore = newMatchDialogView.findViewById(R.id.local_score)
        visitorScore = newMatchDialogView.findViewById(R.id.visitor_score)

        localImage.load(myUser.userImage, R.drawable.ic_incognito, true)
        localUserName.text = myUser.userName

        visitorImage.load(fallbackImage = R.drawable.ic_incognito)
        visitorImage.setOnClickListener {
            userSelectorDialog = UserSelectorDialog(getEligibleUsers(), userSelectorBus, UserSelectorDialog.VISITOR, visitor)
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
            visitor?.let {
                try {
                    openDateSelectionDialog(
                        context,
                        MatchRecord(
                            local = myUser,
                            visitor = visitor,
                            localScore = localScore.text.toString().toInt(),
                            visitorScore = visitorScore.text.toString().toInt()
                        )
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, R.string.invalid_score, Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, R.string.select_user, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Subscribe
    fun onUserSelected(event: UserSelectorDialog.UserSelectedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                selectedUsers.add(event.user)
                visitor = event.user
                visitorImage.load(url = event.user.userImage, rounded = true, fallbackImage = R.drawable.ic_incognito)
                visitorUserName.text = event.user.userName
            }
            else -> return
        }
    }

    @Subscribe
    fun onUserRemoved(event: UserSelectorDialog.UserRemovedEvent) {
        when (event.userType) {
            UserSelectorDialog.VISITOR -> {
                selectedUsers.remove(visitor)
                visitor = null
                visitorImage.load(fallbackImage = R.drawable.ic_incognito)
                visitorUserName.setText(R.string.visitor)
            }
            else -> return
        }
    }
}
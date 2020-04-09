package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.UsersListAdapter
import com.tal.android.pingpong.utils.*
import org.greenrobot.eventbus.Subscribe

class UserSelectorDialog(private val users: List<User>, private val bus: Bus, @UserType private val userType: Int) {

    private var dialog: AlertDialog? = null
    private val usersAdapterBus = Bus.newInstance
    private var selectedUser: User? = null

    fun show(context: Context) {
        usersAdapterBus.register(this)
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.user_selector_dialog, null)

        val usersList: RecyclerView = view.findViewById(R.id.users_list)
        usersList.adapter = UsersListAdapter(users, usersAdapterBus)

        dialog = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.edit_match_details)
            .setView(view)
            .setNeutralButtonText(R.string.cancel)
            .setPositiveButtonText(R.string.accept)
            .setPositiveButtonListener {
                selectedUser?.let {
                    bus.post(UserSelectedEvent(userType, it))
                }
                dismiss()
            }
            .setOnDismissListener(DialogInterface.OnDismissListener {
                usersAdapterBus.unregister(this)
            })
            .build(context)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        selectedUser = event.user
    }

    class UserSelectedEvent(@UserType val userType: Int, val user: User)

    companion object {
        /**
         * Possible user types
         */
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(LOCAL_COMPANION, VISITOR, VISITOR_COMPANION)
        annotation class UserType

        const val LOCAL_COMPANION = 0
        const val VISITOR = 1
        const val VISITOR_COMPANION = 2
    }
}

package com.tal.android.pingpong.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.adapters.recyclerview.UsersListAdapter
import com.tal.android.pingpong.utils.*
import org.greenrobot.eventbus.Subscribe

class UserSelectorDialog(
    private val users: MutableList<User>,
    private val bus: Bus, @UserType
    private val userType: Int,
    private var selectedUser: User? = null
) : BaseDialog() {

    private val usersAdapterBus = Bus.newInstance
    private lateinit var usersList: RecyclerView

    fun show(context: Context) {
        usersAdapterBus.register(this)
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.user_selector_dialog, null)

        usersList = view.findViewById(R.id.users_list)
        refreshUsersList(users)

        val dialogBuilder = DialogFactory
            .Builder()
            .setCancelable(true)
            .setAutoDismiss(false)
            .setTitle(R.string.select_user)
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
        if (selectedUser != null) {
            dialogBuilder
                .setNegativeButtonText(R.string.remove)
                .setNegativeButtonListener {
                    bus.post(UserRemovedEvent(userType))
                    dismiss()
                }
        }
        dialog = dialogBuilder.build(context)
        dialog?.show()
    }

    @Subscribe
    fun onUserClicked(event: UsersListAdapter.UserClickedEvent) {
        selectedUser = event.user
    }

    fun refreshUsersList(eligibleUsers: MutableList<User>) {
        selectedUser?.let {
            eligibleUsers.add(0, it)
        }
        usersList.adapter = UsersListAdapter(
            eligibleUsers,
            usersAdapterBus,
            true,
            selectedUser
        )
    }

    class UserSelectedEvent(@UserType val userType: Int, val user: User)
    class UserRemovedEvent(@UserType val userType: Int)

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

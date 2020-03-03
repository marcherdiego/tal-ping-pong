package com.tal.android.pingpong.data

import com.google.firebase.database.*
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.domain.User

object UsersManager {
    const val EMPTY_STRING = ""

    val users = mutableListOf<User>()
    lateinit var usersDatabase: DatabaseReference

    fun init() {
        usersDatabase = FirebaseDatabase
            .getInstance()
            .reference
            .child("users")
        fetchUsers()
    }

    private fun fetchUsers() {
        usersDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val genericType = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                val keys = dataSnapshot.getValue(genericType)
                synchronized(users) {
                    users.clear()
                    keys?.forEach {
                        dataSnapshot
                            .child(it.key)
                            .getValue(User::class.java)
                            ?.let { product ->
                                users.add(product)
                            }
                    }
                    users.sortWith(Comparator { first, second ->
                        first.userName?.compareTo(second.userName ?: EMPTY_STRING) ?: 0
                    })
                }
                Bus.defaultBus.post(ProductsListUpdatedEvent())
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    fun getUser(email: String?) = users.firstOrNull {
        it.userEmail == email
    }

    fun saveUser(user: User?) {
        val user = user ?: return
        val userKey = getUserKey(user) ?: return
        user.userKey = userKey
        usersDatabase
            .child(userKey)
            .setValue(user)
    }

    fun getUserKey(user: User) = user.userKey ?: usersDatabase.push().key

    class ProductsListUpdatedEvent
}
package com.tal.android.pingpong.data

import com.google.firebase.database.*
import com.nerdscorner.mvplib.events.bus.Bus
import com.tal.android.pingpong.domain.Challenge

object ChallengesManager {
    val challenges = mutableListOf<Challenge>()
    lateinit var entitiesDatabase: DatabaseReference

    fun init() {
        entitiesDatabase = FirebaseDatabase
            .getInstance()
            .reference
            .child("challenges")
        fetch()
    }

    private fun fetch() {
        entitiesDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val genericType = object : GenericTypeIndicator<HashMap<String, Any>>() {}
                val keys = dataSnapshot.getValue(genericType)
                synchronized(challenges) {
                    challenges.clear()
                    keys?.forEach {
                        dataSnapshot
                            .child(it.key)
                            .getValue(Challenge::class.java)
                            ?.let { challenge ->
                                challenges.add(challenge)
                            }
                    }
                    challenges.sortWith(Comparator { first, second ->
                        first.matchDate?.compareTo(second.matchDate) ?: 0
                    })
                }
                Bus.defaultBus.post(ChallengesListUpdatedEvent())
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    fun get(challengeKey: String?) = challenges.firstOrNull {
        it.challengeKey == challengeKey
    }

    fun save(challenge: Challenge?) {
        val challenge = challenge ?: return
        val userKey = getKey(challenge) ?: return
        challenge.challengeKey = userKey
        entitiesDatabase
            .child(userKey)
            .setValue(challenge)
    }

    fun getKey(challenge: Challenge) = challenge.challengeKey ?: entitiesDatabase.push().key

    class ChallengesListUpdatedEvent
}
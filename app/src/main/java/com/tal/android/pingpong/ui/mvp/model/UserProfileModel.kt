package com.tal.android.pingpong.ui.mvp.model

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.tal.android.pingpong.domain.Match
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.extensions.enqueueResponseNotNull
import com.tal.android.pingpong.extensions.fireAndForget
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.networking.services.MatchesService
import com.tal.android.pingpong.networking.services.UsersService
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UserProfileModel(
    private val googleSignInClient: GoogleSignInClient,
    private val sharedPreferences: SharedPreferencesUtils,
    val user: User
) : BaseModel() {

    private val usersService = ServiceGenerator.createService(UsersService::class.java)
    private val matchesService = ServiceGenerator.createService(MatchesService::class.java)

    fun getLoggedUser() = sharedPreferences.getUser()

    fun logout() {
        val user = getLoggedUser() ?: return
        sharedPreferences.saveUser(null)
        googleSignInClient.signOut()
        usersService
            .logout(user)
            .fireAndForget()
    }

    fun fetchLastMatches() {
        matchesService
            .getLastTenMatches(user.userId ?: return)
            .enqueueResponseNotNull(
                success = {
                    extractMatchesStats(it)
                    bus.post(LastTenMatchesFetchedSuccessfullyEvent(it))
                },
                fail = {
                    bus.post(LastTenMatchesFetchFailedEvent())
                },
                model = this
            )
    }

    private fun extractMatchesStats(matches: List<Match>) {
        matches.forEach {
            it.match?.let { match ->
                if (match.local?.userId == user.userId) {
                    // I'm local
                    if (match.localScore > match.visitorScore) {
                        user.matchesWon++
                    } else {
                        user.matchesLost++
                    }
                } else {
                    // I'm visitor
                    if (match.visitorScore > match.localScore) {
                        user.matchesWon++
                    } else {
                        user.matchesLost++
                    }
                }
            }
        }
    }

    class LastTenMatchesFetchedSuccessfullyEvent(val matches: List<Match>)
    class LastTenMatchesFetchFailedEvent
}

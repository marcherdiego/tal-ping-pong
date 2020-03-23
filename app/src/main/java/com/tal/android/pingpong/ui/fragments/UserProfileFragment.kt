package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.domain.User
import com.tal.android.pingpong.ui.mvp.model.UserProfileModel
import com.tal.android.pingpong.ui.mvp.presenter.UserProfilePresenter
import com.tal.android.pingpong.ui.mvp.view.UserProfileView
import com.tal.android.pingpong.utils.SharedPreferencesUtils

class UserProfileFragment : BaseFragment<UserProfilePresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.user_profile_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            val googleSignInClient = GoogleSignIn.getClient(
                it,
                GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestProfile()
                    .requestEmail()
                    .build()
            )
            val sharedPreferences = SharedPreferencesUtils(it)
            val user = (arguments?.getSerializable(USER) as? User) ?: sharedPreferences.getUser() ?: return
            presenter = UserProfilePresenter(
                UserProfileView(this),
                UserProfileModel(googleSignInClient, sharedPreferences, user)
            )
        }
    }

    companion object {
        const val USER = "user"
    }
}

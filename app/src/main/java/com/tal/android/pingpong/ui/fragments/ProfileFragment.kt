package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.ProfileModel
import com.tal.android.pingpong.ui.mvp.presenter.ProfilePresenter
import com.tal.android.pingpong.ui.mvp.view.ProfileView

class ProfileFragment : BaseFragment<ProfilePresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            val googleSignInClient = GoogleSignIn.getClient(
                it,
                GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            )
            val loggedUser = GoogleSignIn.getLastSignedInAccount(it)
            presenter = ProfilePresenter(
                ProfileView(this),
                ProfileModel(googleSignInClient, loggedUser)
            )
        }
    }
}

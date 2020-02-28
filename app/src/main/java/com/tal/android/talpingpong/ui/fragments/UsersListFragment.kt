package com.tal.android.talpingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.talpingpong.R
import com.tal.android.talpingpong.ui.mvp.model.UsersListModel
import com.tal.android.talpingpong.ui.mvp.presenter.UsersListPresenter
import com.tal.android.talpingpong.ui.mvp.view.UsersListView

class UsersListFragment : BaseFragment<UsersListPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.users_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            presenter = UsersListPresenter(
                UsersListView(this),
                UsersListModel(it)
            )
        }
    }
}

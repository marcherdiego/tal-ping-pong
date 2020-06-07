package com.tal.android.pingpong.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nerdscorner.mvplib.events.fragment.BaseFragment
import com.tal.android.pingpong.R
import com.tal.android.pingpong.ui.mvp.model.ReadOnlyUsersListModel
import com.tal.android.pingpong.ui.mvp.presenter.ReadOnlyUsersListPresenter
import com.tal.android.pingpong.ui.mvp.view.ReadOnlyUsersListView
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import com.tal.android.pingpong.utils.multiLet

class ReadOnlyUsersListFragment : BaseFragment<ReadOnlyUsersListPresenter>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.read_only_users_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val championshipId = arguments?.getInt(CHAMPIONSHIP_ID)
        multiLet(championshipId, context) { championshipId, context ->
            presenter = ReadOnlyUsersListPresenter(
                ReadOnlyUsersListView(this),
                ReadOnlyUsersListModel(SharedPreferencesUtils(context), championshipId)
            )
        }
    }

    companion object {
        const val CHAMPIONSHIP_ID = "championship_id"
        const val TITLE = "Members"
    }
}

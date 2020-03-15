package com.tal.android.pingpong.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EmptyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(View(parent.context)) {
        }
    }

    override fun getItemCount() = 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}
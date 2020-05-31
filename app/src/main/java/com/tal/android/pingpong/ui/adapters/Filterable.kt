package com.tal.android.pingpong.ui.adapters

import com.tal.android.pingpong.domain.FilterableObject

interface Filterable<T : FilterableObject> {
    var originalList: MutableList<T>
    var filteredList: List<T>

    fun filter(criteria: String) {
        filteredList = originalList.filter {
            it.matches(criteria)
        }
    }
}

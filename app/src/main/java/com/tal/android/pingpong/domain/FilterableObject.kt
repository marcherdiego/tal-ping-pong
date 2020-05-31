package com.tal.android.pingpong.domain

interface FilterableObject {
    fun matches(criteria: String): Boolean
}

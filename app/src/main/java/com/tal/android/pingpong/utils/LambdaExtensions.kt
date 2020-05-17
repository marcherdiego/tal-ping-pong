package com.tal.android.pingpong.utils

import android.os.Bundle
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun <A, B> Any.multiLet(
    a: A? = null,
    b: B? = null,
    action: (a: A, b: B) -> Unit
): Unit? {
    if (a == null || b == null) {
        return null
    }
    return action(a, b)
}

fun <A, B, C> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    action: (a: A, b: B, c: C) -> Unit
): Unit? {
    if (a == null || b == null || c == null) {
        return null
    }
    return action(a, b, c)
}

fun <A, B, C, D> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    action: (a: A, b: B, c: C, d: D) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null) {
        return null
    }
    return action(a, b, c, d)
}

fun <A, B, C, D, E> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    e: E? = null,
    action: (a: A, b: B, c: C, d: D, e: E) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null || e == null) {
        return null
    }
    return action(a, b, c, d, e)
}

fun <A, B, C, D, E, F> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    e: E? = null,
    f: F? = null,
    action: (a: A, b: B, c: C, d: D, e: E, f: F) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null || e == null || f == null) {
        return null
    }
    return action(a, b, c, d, e, f)
}

fun <A, B, C, D, E, F, G> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    e: E? = null,
    f: F? = null,
    g: G? = null,
    action: (a: A, b: B, c: C, d: D, e: E, f: F, g: G) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null || e == null || f == null || g == null) {
        return null
    }
    return action(a, b, c, d, e, f, g)
}

fun <A, B, C, D, E, F, G, H> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    e: E? = null,
    f: F? = null,
    g: G? = null,
    h: H? = null,
    action: (a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null || e == null || f == null || g == null || h == null) {
        return null
    }
    return action(a, b, c, d, e, f, g, h)
}

fun <A, B, C, D, E, F, G, H, I> Any.multiLet(
    a: A? = null,
    b: B? = null,
    c: C? = null,
    d: D? = null,
    e: E? = null,
    f: F? = null,
    g: G? = null,
    h: H? = null,
    i: I? = null,
    action: (a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I) -> Unit
): Unit? {
    if (a == null || b == null || c == null || d == null || e == null || f == null || g == null || h == null || i == null) {
        return null
    }
    return action(a, b, c, d, e, f, g, h, i)
}

inline fun Any.bundle(block: Bundle.() -> Unit): Bundle {
    return Bundle().apply {
        block()
    }
}

package com.secondhand.vip.util

import android.content.Context

object FavoriteStore {

    private const val PREF = "favorites"

    fun toggle(context: Context, id: String): Boolean {
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val set = sp.getStringSet("ids", mutableSetOf())!!.toMutableSet()

        val added =
            if (set.contains(id)) {
                set.remove(id)
                false
            } else {
                set.add(id)
                true
            }

        sp.edit().putStringSet("ids", set).apply()
        return added
    }

    fun isFavorite(context: Context, id: String): Boolean {
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return sp.getStringSet("ids", setOf())!!.contains(id)
    }
}

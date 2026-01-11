package com.secondhand.vip.util

import android.content.Context

object FavoriteManager {

    private const val PREF_NAME = "favorites_pref"
    private const val KEY_FAVORITES = "favorites"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isFavorite(context: Context, productId: String): Boolean {
        val set = prefs(context).getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return set.contains(productId)
    }

    fun toggle(context: Context, productId: String): Boolean {
        val pref = prefs(context)
        val set = pref.getStringSet(KEY_FAVORITES, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()

        val isNowFavorite: Boolean

        if (set.contains(productId)) {
            set.remove(productId)
            isNowFavorite = false
        } else {
            set.add(productId)
            isNowFavorite = true
        }

        pref.edit().putStringSet(KEY_FAVORITES, set).apply()
        return isNowFavorite
    }

    fun getAll(context: Context): List<String> {
        return prefs(context)
            .getStringSet(KEY_FAVORITES, emptySet())
            ?.toList()
            ?: emptyList()
    }
}

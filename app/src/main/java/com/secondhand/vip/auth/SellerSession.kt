package com.secondhand.vip.auth

import android.content.Context

object SellerSession {

    private const val PREF = "seller_auth"
    private const val KEY_LOGGED_IN = "isSeller"

    fun setLoggedIn(ctx: Context, value: Boolean) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGGED_IN, value)
            .apply()
    }

    fun isLoggedIn(ctx: Context): Boolean {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED_IN, false)
    }

    fun logout(ctx: Context) {
        setLoggedIn(ctx, false)
    }
}

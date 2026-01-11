package com.secondhand.vip.auth

import android.content.Context

object AuthManager {

    private const val PREF = "seller_auth"
    private const val KEY_LOGIN = "is_login"

    fun login(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGIN, true)
            .apply()
    }

    fun logout(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun isLogin(ctx: Context): Boolean {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGIN, false)
    }
}

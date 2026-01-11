package com.secondhand.vip.util

import android.content.Context

object Auth {

    fun isSeller(ctx: Context): Boolean {
        val sp = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sp.getBoolean("isSeller", false)
    }

    fun logout(ctx: Context) {
        ctx.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}

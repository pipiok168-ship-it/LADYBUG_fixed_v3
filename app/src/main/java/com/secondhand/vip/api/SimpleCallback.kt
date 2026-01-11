package com.secondhand.vip.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SimpleCallback<T>(
    private val onSuccess: (T) -> Unit,
    private val onError: (() -> Unit)? = null
) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
            val body = response.body()
            if (response.isSuccessful && body != null) {
                onSuccess(body)
            } else {
                onError?.invoke()
            }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onError?.invoke()
    }
}

package com.secondhand.vip.api

import com.secondhand.vip.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // =============================
    // 取得商品列表
    // =============================
    @GET("/api/products")
    fun getProducts(): Call<List<Product>>

    // =============================
    // 新增商品（多圖）
    // =============================
    @Multipart
    @POST("/api/products")
    fun addProduct(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<Product>

    // =============================
    // 刪除商品
    // =============================
    @DELETE("/api/products/{id}")
    fun deleteProduct(
        @Path("id") id: String
    ): Call<Void>
}

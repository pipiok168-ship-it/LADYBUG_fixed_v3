package com.secondhand.vip.api

import com.secondhand.vip.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ===== 商品列表 =====
    @GET("/api/products")
    fun getProducts(): Call<List<Product>>

    // ===== 取得單一商品（商品詳情用）=====
    @GET("/api/products/{id}")
    fun getProductById(
        @Path("id") id: String    // 一律傳 MongoDB _id
    ): Call<Product>

    // ===== 新增商品（多圖）=====
    @Multipart
    @POST("/api/products")
    fun addProduct(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<Product>

    // ===== 刪除商品 =====
    @DELETE("/api/products/{id}")
    fun deleteProduct(
        @Path("id") id: String    // 一律傳 MongoDB _id
    ): Call<Void>
}

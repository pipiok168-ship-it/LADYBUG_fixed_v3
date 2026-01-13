package com.secondhand.vip.api

import com.secondhand.vip.model.AddProductRequest
import com.secondhand.vip.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // 取得商品列表
    @GET("/api/products")
    fun getProducts(): Call<List<Product>>

    // 新增商品（JSON only）
    @POST("/api/products")
    fun addProduct(
        @Body body: AddProductRequest
    ): Call<Product>

    // 刪除商品
    @DELETE("/api/products/{id}")
    fun deleteProduct(
        @Path("id") id: String
    ): Call<Void>
}

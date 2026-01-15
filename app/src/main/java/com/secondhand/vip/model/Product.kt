package com.secondhand.vip.model

import java.io.Serializable

data class Product(
    val _id: String,            // ★ 唯一 ID，對齊 MongoDB
    val name: String,
    val price: Int,
    val description: String,
    val imageUrls: List<String> = emptyList()
) : Serializable

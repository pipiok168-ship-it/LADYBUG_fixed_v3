package com.secondhand.vip.model

import java.io.Serializable

data class Product(
    val _id: String? = null,
    val name: String? = "",
    val price: Int? = 0,
    val imageUrl: String? = "",
    val description: String? = ""
) : Serializable

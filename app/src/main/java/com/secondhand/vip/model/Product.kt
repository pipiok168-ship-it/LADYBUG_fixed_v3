package com.secondhand.vip.model

import java.io.Serializable

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val description: String
) : Serializable

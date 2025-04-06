package com.example.shopnest.data

import androidx.compose.runtime.MutableState

data class Product(
    val name: String,
    val category: String,
    val description: String,
    val imageResId: Int,
    val price: Double
)

data class CartItem(val product: Product, var quantity: Int)




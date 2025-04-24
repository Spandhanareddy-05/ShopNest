package com.example.shopnest.data

data class OrderSummary(
    val id: String,
    val date: String,
    val total: Double,
    val items: List<String>
)


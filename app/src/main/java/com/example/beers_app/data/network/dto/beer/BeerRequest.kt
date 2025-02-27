package com.example.beers_app.data.network.dto.beer

// body for post request
data class BeerRequest(
    val alcPercentage: Double,
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val category: String,
    val salePercentage: Double?,
    val imagePath: String?
)
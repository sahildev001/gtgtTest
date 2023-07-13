package com.sahil.wetherapplication.data.model

data class WeatherData(
    val cityName: String,
    val temperature: Double,
    val description: String,
    val iconUrl: String,
)

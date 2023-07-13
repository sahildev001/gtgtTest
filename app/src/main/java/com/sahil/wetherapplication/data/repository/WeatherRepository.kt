package com.sahil.wetherapplication.data.repository

import com.sahil.wetherapplication.data.model.WeatherData
import com.sahil.wetherapplication.data.model.WeatherResponse
import com.sahil.wetherapplication.network.ApiService
import com.sahil.wetherapplication.network.RetrofitClient
import com.sahil.wetherapplication.util.Resourse
import org.json.JSONObject

class WeatherRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getWeatherDataByCity(city: String): Resourse<WeatherData> {
        try {
            val response = apiService.getWeatherByCity(city, API_KEY)
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                return Resourse.Success(weatherResponse?.toWeatherData())
            } else{
                return Resourse.Error(response.errorBody()?.string()
                    ?.let { JSONObject(it).getString("message") })
            }
        }catch (e:Exception){
            return Resourse.Error(e.message)
        }
    }

    private fun WeatherResponse.toWeatherData(): WeatherData {
        val cityName = name
        val temperature = main.temp
        val description = weather[0].description
        val iconUrl = "https://openweathermap.org/img/wn/${weather[0].icon}.png"
        return WeatherData(cityName, temperature, description, iconUrl)
    }

    companion object {
        private const val API_KEY = "bfbd858bf514db32dd986981c80aa7a5"
    }
}

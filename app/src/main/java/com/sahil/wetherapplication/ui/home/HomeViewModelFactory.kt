package com.sahil.wetherapplication.ui.home

import androidx.lifecycle.ViewModel
import com.sahil.wetherapplication.data.repository.WeatherRepository

import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val weatherRepository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}

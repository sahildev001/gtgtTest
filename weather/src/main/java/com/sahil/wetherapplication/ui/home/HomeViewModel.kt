package com.sahil.wetherapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahil.wetherapplication.data.repository.WeatherRepository
import com.sahil.wetherapplication.util.Resourse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _weatherState = MutableStateFlow(WhetherState())
    val weatherState: StateFlow<WhetherState> get() = _weatherState

    fun fetchWeatherDataByCity(city: String) {
        viewModelScope.launch {
            try {
                _weatherState.value = WhetherState(isLoading = true)
                when (val response = weatherRepository.getWeatherDataByCity(city)) {
                    is Resourse.Success -> {
                        _weatherState.value = WhetherState(whetherInfo = response.data)
                    }
                    is Resourse.Error -> {
                        _weatherState.value = WhetherState(error = response.message)
                    }
                }
            } catch (e: Exception) {
                _weatherState.value = WhetherState(error = "An error occurred: ${e.message}")
            } finally {
                _weatherState.value = _weatherState.value.copy(isLoading = false)
            }
        }
    }
}

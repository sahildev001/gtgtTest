package com.sahil.wetherapplication.ui.home

import com.sahil.wetherapplication.data.model.WeatherData

data class WhetherState(
    val whetherInfo: WeatherData? = null,
    val isLoading:Boolean = false,
    val error:String?= null,

)

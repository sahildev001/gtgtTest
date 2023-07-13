package com.sahil.wetherapplication.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sahil.wetherapplication.R
import com.sahil.wetherapplication.data.model.WeatherData
import com.sahil.wetherapplication.data.repository.WeatherRepository
import com.sahil.wetherapplication.databinding.ActivityHomeBinding

import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

      init()
setData()

        val lastSearchedCity = getLastSearchedCity() // Retrieve the last searched city from preferences
        homeViewModel.fetchWeatherDataByCity(lastSearchedCity)

    buildSearch()
    }

    private fun init(){
        val weatherRepository = WeatherRepository()
        val viewModelFactory = HomeViewModelFactory(weatherRepository)
        homeViewModel = ViewModelProvider(this, viewModelFactory)
            .get(HomeViewModel::class.java)
    }

    private fun setData(){
        lifecycleScope.launch {
            homeViewModel.weatherState.collect { state ->
                state.whetherInfo?.let { weatherData ->
                    displayWeatherData(weatherData,null)
                }
                state.error?.let {
                    displayWeatherData(null,it)
                }
                if(state.isLoading){
                    binding.progressbar.visibility = View.VISIBLE
                }else{
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }

    private fun displayWeatherData(weatherData: WeatherData?, error: String?) {
        if(error != null){
            binding.tvError.visibility = View.VISIBLE
            binding.llContent.visibility = View.GONE
            binding.tvError.text = error
        }else {
            binding.tvError.visibility = View.GONE
            binding.llContent.visibility = View.VISIBLE
            binding.textViewCityName.text = weatherData?.cityName
            binding.textViewTemperature.text =
                getString(R.string.temperature_format, weatherData?.temperature ?: 0)
            binding.textViewDescription.text = weatherData?.description

            Log.e(TAG, "displayWeatherData: ${weatherData?.iconUrl}",)
            ContextCompat.getDrawable(this@HomeActivity,R.drawable.ic_refresh)?.let {
                Picasso.get()
                    .load(weatherData?.iconUrl)
                    .error(it)
                    .resize(100, 100)
                    .centerCrop()
                    .into(binding.imageViewWeatherIcon)
            }

        }

    }

    private fun buildSearch(){
        binding.searchView.queryHint = getString(R.string.search_hint)
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    homeViewModel.fetchWeatherDataByCity(query)
                    saveLastSearchedCity(query)
                    binding.searchView.clearFocus()
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    private fun getLastSearchedCity(): String {
        val sharedPreferences = getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        return sharedPreferences.getString("lastSearchedCity", "London") ?: ""
    }

    private fun saveLastSearchedCity(city: String) {
        val sharedPreferences = getSharedPreferences("WeatherApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lastSearchedCity", city)
        editor.apply()
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}



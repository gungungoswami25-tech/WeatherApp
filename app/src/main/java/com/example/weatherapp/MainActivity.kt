package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCity = findViewById<EditText>(R.id.etCity)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val tvCityName = findViewById<TextView>(R.id.tvCityName)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWind = findViewById<TextView>(R.id.tvWind)

        btnSearch.setOnClickListener {
            val city = etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeather(city, tvCityName, tvTemperature, tvCondition, tvHumidity, tvWind)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeather(
        city: String,
        tvCity: TextView,
        tvTemp: TextView,
        tvCond: TextView,
        tvHum: TextView,
        tvWind: TextView
    ) {
        val url = "https://wttr.in/$city?format=j1"

        thread {
            try {
                val apiResponse = URL(url).readText()
                val jsonObject = JSONObject(apiResponse)

                val currentCondition = jsonObject.getJSONArray("current_condition").getJSONObject(0)
                val tempC = currentCondition.getString("temp_C")
                val humidity = currentCondition.getString("humidity")
                val windSpeed = currentCondition.getString("windspeedKmph")
                val conditionDesc = currentCondition.getJSONArray("weatherDesc").getJSONObject(0).getString("value")

                runOnUiThread {
                    // Aapka type kiya hua city name hi screen par show hoga
                    tvCity.text = city.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    tvTemp.text = "$tempC °C"
                    tvCond.text = conditionDesc
                    tvHum.text = "Humidity: $humidity%"
                    tvWind.text = "Wind: $windSpeed km/h"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "City not found or check internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
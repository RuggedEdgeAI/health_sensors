package com.isafemobile.test.sensor.heartrate.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.isafemobile.test.sensor.heartrate.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val healthViewModel: HealthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observer()

        healthViewModel.getInitial()
        healthViewModel.startMonitoring()
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        lifecycleScope.launch {
            healthViewModel.heartRate.collect { rate ->
                binding.textViewHeartRate.text = "$rate/min"
                val fadeInOut = AlphaAnimation(1f, 0f).apply {
                    duration = 300
                    repeatMode = AlphaAnimation.REVERSE
                    repeatCount = 1
                }
                binding.imageViewHeartRate.startAnimation(fadeInOut)
            }
        }

        lifecycleScope.launch {
            healthViewModel.spO2.collect { spo ->
                binding.textViewSpo2.text = "$spo%"
                val fadeInOut = AlphaAnimation(1f, 0f).apply {
                    duration = 300
                    repeatMode = AlphaAnimation.REVERSE
                    repeatCount = 1
                }
                binding.imageViewSpo2.startAnimation(fadeInOut)
            }
        }
    }
}

package com.isafemobile.test.sensor.heartrate.main

import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.isafemobile.test.sensor.heartrate.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val heartRateViewModel: HeartRateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observer()

        heartRateViewModel.getInitial()
        binding.buttonStart.setOnClickListener {
            binding.buttonStart.isEnabled = false
            binding.buttonStart.setText("Detecting...")
            heartRateViewModel.startMonitoring()
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            heartRateViewModel.heartRate.collect { rate ->
                binding.textViewHeartRate.text = "$rate/min"
                binding.buttonStart.isEnabled = true
                binding.buttonStart.setText("Start")
                val fadeInOut = AlphaAnimation(1f, 0f).apply {
                    duration = 300 // Duration of flash effect
                    repeatMode = AlphaAnimation.REVERSE
                    repeatCount = 1
                }
                binding.imageViewHeartRate.startAnimation(fadeInOut)
            }
        }
    }
}

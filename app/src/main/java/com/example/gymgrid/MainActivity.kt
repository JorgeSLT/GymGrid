package com.example.gymgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.gymgrid.databinding.BarsBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: BarsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = BarsBinding.inflate(layoutInflater)


        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}

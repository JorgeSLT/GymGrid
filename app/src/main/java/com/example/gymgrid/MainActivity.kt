package com.example.gymgrid

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.gymgrid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val currentDestinationId = navController.currentDestination?.id

        // Que fragmento
        when (currentDestinationId) {
            R.id.homeFragment -> {
                navController.navigate(R.id.action_homeFragment_to_trainingFragment)
            }

            R.id.trainingFragment -> {
                navController.navigate(R.id.action_trainingFragment_to_homeFragment)
            }

            else -> {

            }
        }

    }
}
package com.example.gymgrid

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.gymgrid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupButtonActions()
        setupToolbarTitle()
    }

    private fun setupButtonActions() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.homeButton.setOnClickListener {
            navigateToFragment(R.id.action_global_homeFragment)
        }

        binding.startButton.setOnClickListener {
            navigateToFragment(R.id.action_global_trainingFragment)
        }

        binding.calendarButton.setOnClickListener {
            navigateToFragment(R.id.action_global_calendarFragment)
        }

        binding.routineButton.setOnClickListener {
            navigateToFragment(R.id.action_global_routineFragment)
        }
    }

    private fun setupToolbarTitle() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val title = when (destination.id) {
                R.id.homeFragment -> "Inicio"
                R.id.trainingFragment -> "Entrenamiento"
                R.id.calendarFragment -> "Calendario"
                R.id.routineFragment -> "Rutina"
                R.id.profileFragment -> "Perfil"

                else -> "Título de Página"
            }
            binding.toolbarTitle.text = title
        }
    }

    private fun navigateToFragment(actionId: Int) {
        findNavController(R.id.nav_host_fragment_content_main).navigate(actionId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_perfil -> {
                navigateToFragment(R.id.action_global_profileFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

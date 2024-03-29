package com.example.gymgrid

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.gymgrid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar // Asumiendo que tienes un Toolbar en tu layout con el id 'toolbar'
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Oculta el título predeterminado de la ActionBar

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Configuración de la acción del botón de inicio
        binding.startButton.setOnClickListener { _ ->
            val currentDestinationId = navController.currentDestination?.id
            when (currentDestinationId) {
                R.id.homeFragment -> navController.navigate(R.id.action_homeFragment_to_trainingFragment)
                R.id.trainingFragment -> navController.navigate(R.id.action_trainingFragment_to_homeFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_perfil -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)

                navController.navigate(R.id.action_global_perfilFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

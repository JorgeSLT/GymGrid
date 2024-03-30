    package com.example.gymgrid

    import android.content.Context
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.Menu
    import android.view.MenuItem
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.widget.Toolbar
    import androidx.navigation.findNavController
    import com.example.gymgrid.databinding.ActivityMainBinding
    import android.widget.EditText
    import androidx.appcompat.app.AlertDialog

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val toolbar: Toolbar = binding.toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            if (isFirstTime()) {
                askForUserName()
            }

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

        private fun askForUserName() {
            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(R.layout.dialog_custom_layout, null)
            val editText = view.findViewById<EditText>(R.id.editTextUserName)

            AlertDialog.Builder(this)
                .setTitle("Bienvenido")
                .setMessage("¿Cómo te llamas?")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ ->
                    val name = editText.text.toString().trim()
                    if (name.isNotEmpty()) {
                        saveUserName(name)
                        askForFitnessGoal()
                    }
                }
                .create().show()
        }

        private fun saveUserName(name: String) {
            val sharedPref = getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("USER_NAME", name)
                putBoolean("FIRST_TIME", false)
                apply()
            }
        }

        private fun askForFitnessGoal() {
            val goals = arrayOf("Perder peso", "Ganar músculo")
            AlertDialog.Builder(this)
                .setTitle("Selecciona tu objetivo")
                .setSingleChoiceItems(goals, -1) { dialog, which ->
                    saveFitnessGoal(goals[which])
                    dialog.dismiss()
                    askForTrainingDays()
                }
                .setCancelable(false)
                .create()
                .show()
        }


        private fun saveFitnessGoal(goal: String) {
            val sharedPref = getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("FITNESS_GOAL", goal)
                apply()
            }
        }

        private fun askForTrainingDays() {
            val daysOptions = arrayOf("3 días", "5 días")
            AlertDialog.Builder(this)
                .setTitle("¿Cuántos días piensas entrenar a la semana?")
                .setSingleChoiceItems(daysOptions, -1) { dialog, which ->
                    saveTrainingDays(daysOptions[which])
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
        }


        private fun saveTrainingDays(days: String) {
            val sharedPref = getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
            with(sharedPref.edit()) {
                putString("TRAINING_DAYS", days)
                apply()
            }
        }

        private fun isFirstTime(): Boolean {
            val sharedPref = getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
            return sharedPref.getBoolean("FIRST_TIME", true)
        }
    }

package com.example.gymgrid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.gymgrid.database.AppDatabase
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.databinding.ActivityMainBinding
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.gymgrid.database.entities.Dia
import com.example.gymgrid.database.entities.DiaEjercicioRelacion
import com.example.gymgrid.database.entities.Rutina
import com.example.gymgrid.database.entities.RutinaDiaRelacion
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            lifecycleScope.launch(Dispatchers.IO) {
                initializeDatabaseAndData(applicationContext)
            }
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
                else -> "GymGrid"
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

    private fun initializeDatabaseAndData(context: Context) {
        val db = AppDatabase.getDatabase(context)
        inicializarEjercicios(db)
        inicializarDiasYAsignarEjercicios(db)
        inicializarRutinasYAsociarDias(db)

    }

    private fun inicializarEjercicios(db: AppDatabase) {
        val ejercicios = listOf(
            Ejercicio(titulo = "Burpees", descripcion = "Una serie intensa de ejercicio que implica una combinación de flexiones y un salto vertical.", imagen = "burpees", tieneRepeticiones = true, repeticiones = 10),
            Ejercicio(titulo = "Plancha", descripcion = "Ejercicio isométrico que se centra en fortalecer el core. Mantén tu cuerpo recto y elevado, apoyándote en tus codos y puntas de los pies.", imagen = "plancha", tieneRepeticiones = false, repeticiones = null),
            Ejercicio(titulo = "Flexiones", descripcion = "Ejercicio básico de calistenia que trabaja pecho, hombros y tríceps. Baja tu cuerpo hacia el suelo con las manos al ancho de los hombros y empuja hacia arriba.", imagen = "flexiones", tieneRepeticiones = true, repeticiones = 15),
            Ejercicio(titulo = "Rodillas al pecho", descripcion = "Ejercicio cardiovascular que implica llevar las rodillas hacia el pecho de forma alternada, ayudando a fortalecer el abdomen.", imagen = "rodillas", tieneRepeticiones = true, repeticiones = 20),
            Ejercicio(titulo = "Sentadilla", descripcion = "Desde una posición de pie, flexiona las rodillas y baja el cuerpo como si te sentaras en una silla.", imagen = "sentadilla", tieneRepeticiones = true, repeticiones = 15),
            Ejercicio(titulo = "Mountain climbers", descripcion = "En posición de plancha, alterna llevando cada rodilla hacia el pecho rápidamente.", imagen = "mountain", tieneRepeticiones = true, repeticiones = 20),
            Ejercicio(titulo = "Extensiones hombro", descripcion = "Pueden realizarse con mancuernas, elevando los brazos lateralmente desde el cuerpo.", imagen = "hombro", tieneRepeticiones = true, repeticiones = 10),
            Ejercicio(titulo = "Dominadas", descripcion = "Cuelga de una barra con las manos más anchas que los hombros y eleva tu cuerpo hasta que la barbilla supere la barra.", imagen = "dominadas", tieneRepeticiones = true, repeticiones = 8),
            Ejercicio(titulo = "Fondos", descripcion = "Utilizando dos barras paralelas, baja tu cuerpo manteniendo los codos cerca del torso y luego empuja hacia arriba para levantarte.", imagen = "fondos", tieneRepeticiones = true, repeticiones = 10),
            Ejercicio(titulo = "Flexiones en diamante", descripcion = "Coloca las manos juntas debajo del pecho formando un diamante con los dedos y realiza la flexión.", imagen = "diamante", tieneRepeticiones = true, repeticiones = 12),
            Ejercicio(titulo = "Press banca", descripcion = "Acostado en una banca, levanta una barra desde el pecho hasta extender completamente los brazos.", imagen = "press", tieneRepeticiones = true, repeticiones = 12)
        )
        db.gymDao().insertarEjercicios(ejercicios)
    }

    private fun inicializarDiasYAsignarEjercicios(db: AppDatabase) {
        // Definición de los días y los ejercicios asignados a cada uno
        val asignacionesDiasEjercicios = listOf(
            listOf("Burpees", "Plancha", "Flexiones"),
            listOf("Rodillas al pecho", "Sentadilla", "Plancha"),
            listOf("Flexiones", "Mountain climbers", "Burpees"),
            listOf("Mountain climbers", "Flexiones", "Sentadilla"),
            listOf("Flexiones", "Extensiones hombro", "Sentadilla"),
            listOf("Dominadas", "Fondos", "Flexiones en diamante"),
            listOf("Sentadilla", "Press banca", "Dominadas"),
            listOf("Extensiones hombro", "Press banca", "Dominadas"),
        )

        asignacionesDiasEjercicios.forEachIndexed { index, listaEjercicios ->
            val diaId = db.gymDao().insertarDia(Dia(nombreDia = "Día ${index + 1}"))
            listaEjercicios.forEach { tituloEjercicio ->
                val ejercicioId = db.gymDao().obtenerIdEjercicioPorTitulo(tituloEjercicio)
                db.gymDao().insertarDiaEjercicioRelacion(DiaEjercicioRelacion(diaId = diaId, ejercicioId = ejercicioId))
            }
        }
    }

    private fun inicializarRutinasYAsociarDias(db: AppDatabase) {
        // Lista de rutinas y los nombres de los días asociados
        val rutinasYDias = listOf(
            Triple("Rutina 3 días peso", "Rutina de 3 días para perder peso", listOf("Día 1", "Día 2", "Día 3")),
            Triple("Rutina 3 días músculo", "Rutina de 3 días para ganar musculo", listOf("Día 5", "Día 6", "Día 8")),
            Triple("Rutina 5 días peso", "Rutina de 5 días para perder peso", listOf("Día 1", "Día 2", "Día 3", "Día 4", "Día 8")),
            Triple("Rutina 5 días músculo", "Rutina de 5 días para ganar musculo", listOf("Día 2", "Día 5", "Día 6", "Día 7", "Día 8"))
        )

        rutinasYDias.forEach { (nombre, descripcion, nombresDias) ->
            val rutinaId = db.gymDao().insertarRutina(Rutina(nombre = nombre, descripcion = descripcion))
            nombresDias.forEach { nombreDia ->
                val diaId = db.gymDao().obtenerIdDiaPorNombre(nombreDia)
                db.gymDao().insertarRutinaDiaRelacion(RutinaDiaRelacion(rutinaId = rutinaId, diaId = diaId))
            }
        }
    }


}

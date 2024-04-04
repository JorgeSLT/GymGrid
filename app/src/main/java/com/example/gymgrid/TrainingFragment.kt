package com.example.gymgrid

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymgrid.database.AppDatabase
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.databinding.TrainingFragmentBinding
import java.util.Calendar
import android.os.Handler

class TrainingFragment : Fragment() {
    //Declaracion de las variables binding para la visualizacion del archivo XML
    private var _binding: TrainingFragmentBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null

    //Variables para controlar el ejercicio actual y la lista de ejercicios de hoy
    private var indiceEjercicioActual = 0
    private var ejerciciosHoy: List<Ejercicio> = emptyList()

    //Metodo de creacion de la interfaz de usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrainingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Metodo que sucede a onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Llamada a la carga de la rutina actual
        //Con su subllamada obtengo la lista de ejercicios de hoy
        cargarIdRutinaBasadaEnPreferencias()

        //Configuracion del listener del boton de play
        binding.bigPlayButton.setOnClickListener {
            //Si es que es visible
            if (it.visibility == View.VISIBLE) {
                reproducirSecuenciaSonido()
            }
        }
    }

    //Funcion para determinar que dia de la semana es
    private fun obtenerDiaSemana(): String {
        val diaSemana = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val nombreDia = when (diaSemana) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miercoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sabado"
            Calendar.SUNDAY -> "Domingo"
            else -> ""
        }

        return nombreDia
    }

    //Funcion para cargar rutinas segun las preferencias del usuario
    private fun cargarIdRutinaBasadaEnPreferencias() {
        //Acceso a la BBDD y a las preferencias
        val db = AppDatabase.getDatabase(requireContext())
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)

        //Transformacion de los valores guardados en las preferencias
        val objetivo = when(sharedPref?.getString("FITNESS_GOAL", "")) {
            "Perder peso" -> "perder_peso"
            "Ganar músculo" -> "ganar_musculo"
            else -> ""
        }
        val dias = when(sharedPref?.getString("TRAINING_DAYS", "")) {
            "3 días" -> 3
            "5 días" -> 5
            else -> 0
        }

        //Llamada a la funcion DAO para obtener el id de una rutina
        db.gymDao().getRutinaIdPorObjetivoYDias(objetivo, dias).observe(viewLifecycleOwner) { rutinaId ->
            rutinaId?.let {
                //Llamada a la funcion para cargar dias con sus respectivos ejercicios dado un id de rutina
                cargarDiasYejerciciosDeLaRutinaSeleccionada(it)
            }
        }
    }

    //Funcion para cargar dias con sus respectivos ejercicios dado un id de rutina
    //De esta manera consigo la lista de ejercicios de hoy
    private fun cargarDiasYejerciciosDeLaRutinaSeleccionada(id: Long) {
        val db = AppDatabase.getDatabase(requireContext())
        val nombreDiaActual = obtenerDiaSemana()

        if (id != -1L) {
            db.gymDao().obtenerDiasConEjerciciosPorRutinaId(id).observe(viewLifecycleOwner) { diasConEjercicios ->
                val ejercicios = diasConEjercicios
                    .filter { it.dia.diaSemana == nombreDiaActual }
                    .flatMap { it.ejercicios }

                //Si hoy hay ejercicios se muestra el boton de play
                if (ejercicios.isNotEmpty()) {
                    ejerciciosHoy = ejercicios
                    mostrarBotonPlay()
                //Sino muestra el mensaje de que no hay rutina hoy
                } else {
                    mostrarMensajeNoHayRutina()
                }
            }
        }
    }

    //Funciones para mostrar los diferentes mensajes
    private fun mostrarBotonPlay() {
        binding.bigPlayButton.visibility = View.VISIBLE
        binding.tvNoRutina.visibility = View.GONE
    }

    private fun mostrarMensajeNoHayRutina() {
        binding.tvNoRutina.visibility = View.VISIBLE
        binding.bigPlayButton.visibility = View.GONE
        binding.tvNoRutina.text = "No hay rutina para hoy"
    }

    private fun mostrarImagenEjercicio(nombreImagen: String) {
        binding.bigPlayButton.visibility = View.GONE
        val resourceId = resources.getIdentifier(nombreImagen, "drawable", context?.packageName)
        if (resourceId != 0) {
            binding.imageViewEjercicio.setImageResource(resourceId)
            binding.imageViewEjercicio.visibility = View.VISIBLE
        } else {
            binding.imageViewEjercicio.visibility = View.GONE
        }
    }

    private fun mostrarFinEjercicios() {
        binding.tvNoRutina.visibility = View.VISIBLE
        binding.imageViewEjercicio.visibility = View.GONE
        binding.bigPlayButton.visibility = View.GONE
        binding.tvNoRutina.text = "FIN"
    }

    //Funcion para controlar el sonido de "ticks"
    private fun reproducirSecuenciaSonido() {
        //De nuevo una comprobacion para asegurar
        if (ejerciciosHoy.isEmpty()) {
            mostrarMensajeNoHayRutina()
            return
        }

        //Si se han acabado los ejercicios se muestra el mensaje de FIN
        if (indiceEjercicioActual >= ejerciciosHoy.size) {
            mostrarFinEjercicios()
            return
        }

        //Obtenemos las repeticiones del ejercicio en cuestion
        val ejercicioActual = ejerciciosHoy[indiceEjercicioActual]
        val repeticiones = ejercicioActual.repeticiones ?: return

        //Mostramos la imagen del ejercicio para que el usuario pueda comprender bien lo que hacer
        mostrarImagenEjercicio(ejercicioActual.imagen)

        //Inicializamos el mediaPlayer con el sonido que se elija
        mediaPlayer = MediaPlayer.create(context, R.raw.tick_sound)
        val handler = Handler()
        var contadorRepeticiones = 0

        //Definimos un bloque runnable
        val runnable = object : Runnable {
            override fun run() {
                //Determina si ha hecho todas las repeticiones o no
                if (contadorRepeticiones >= repeticiones) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    indiceEjercicioActual++
                    reproducirSecuenciaSonido()
                } else {
                    mediaPlayer?.start()
                    contadorRepeticiones++
                    handler.postDelayed(this, 2000)
                }
            }
        }
        handler.post(runnable)
    }

    //Funcion para evitar que el sonido siga cuando cambiamos de Fragment
    override fun onStop() {
        super.onStop()
        detenerSonidoYReinicio()
    }

    private fun detenerSonidoYReinicio() {
        val handler = Handler()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
        indiceEjercicioActual = 0
    }

    //Funcion llamada en la destruccion de la vista del Fragment
    //Libera la referencia del binding para evitar problemas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
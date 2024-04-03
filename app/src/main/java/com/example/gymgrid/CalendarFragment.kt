package com.example.gymgrid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymgrid.database.AppDatabase
import com.example.gymgrid.databinding.CalendarFragmentBinding

class CalendarFragment : Fragment() {

    //Declaracion de las variables binding para la visualizacion del archivo XML
    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!

    //Metodo de creacion de la interfaz de usuario
    override fun onCreateView(
        //Se obtiene el layout del Fragment y se devuelve la vista
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Metodo que sucede a onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Llamada a la funcion para cargar rutinas para un usuario en concreto
        cargarIdRutinaBasadaEnPreferencias()
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
    private fun cargarDiasYejerciciosDeLaRutinaSeleccionada(id: Long) {
        val db = AppDatabase.getDatabase(requireContext())

        //Si existe una rutina con dicho id
        if (id != -1L) {
            //Llamada a la funcion DAO para obtener los dias con ejercicios
            db.gymDao().obtenerDiasConEjerciciosPorRutinaId(id).observe(viewLifecycleOwner) { diasConEjercicios ->
                val items = mutableListOf<RutinaItem>()
                //Se guardan los dias en una lista llamada items
                diasConEjercicios.forEach { diaConEjercicios ->
                    items.add(RutinaItem.DiaItem(diaConEjercicios.dia.nombreDia, diaConEjercicios.dia.diaSemana))
                    //Se guardan los ejercicios en la misma lista
                    diaConEjercicios.ejercicios.forEach { ejercicio ->
                        items.add(RutinaItem.EjercicioItem(ejercicio))
                    }
                }
                //Se pasa la lista a la funcion para configurar el RecyclerView
                setupRecyclerView(items)
            }
        }
    }

    //Funcion para configurar el RecyclerView
    private fun setupRecyclerView(items: List<RutinaItem>) {
        //Usamos la clase DiaAdapter que extiende al RecyclerView
        val diaAdapter = DiaAdapter(items, requireContext())

        //Se asigna el adaptador junto con un layout lineal
        binding.rvRutinas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = diaAdapter
        }
    }

    //Funcion llamada en la destruccion de la vista del Fragment
    //Libera la referencia del binding para evitar problemas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

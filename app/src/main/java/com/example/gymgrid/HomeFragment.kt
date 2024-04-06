package com.example.gymgrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gymgrid.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    //Declaracion de las variables binding para la visualizacion del archivo XML
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    //Metodo de creacion de la interfaz de usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Se obtiene el layout del Fragment y se devuelve la vista
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Metodo que sucede a onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        //Listener para el boton que muestra las rutinas disponibles
        binding.rutinasContainer.setOnClickListener {
            navController.navigate(R.id.action_global_introFragment)
        }
    }

    //Funcion llamada en la destruccion de la vista del Fragment
    //Libera la referencia del binding para evitar problemas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

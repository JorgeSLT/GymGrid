package com.example.gymgrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymgrid.database.AppDatabase
import com.example.gymgrid.databinding.IntroFragmentBinding

class IntroFragment : Fragment() {

    // Declaración de las variables binding para la visualización del archivo XML
    private var _binding: IntroFragmentBinding? = null
    private val binding get() = _binding!!

    // Método de creación de la interfaz de usuario
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = IntroFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    // Método que sucede a onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rutinasRecyclerView.layoutManager = LinearLayoutManager(context)
        cargarRutinas()
    }

    // Función para cargar las rutinas desde el adaptador
    private fun cargarRutinas() {
        val db = AppDatabase.getDatabase(requireContext())
        db.gymDao().obtenerTodasLasRutinas().observe(viewLifecycleOwner) { rutinas ->
            binding.rutinasRecyclerView.adapter = RutinaAdapter(rutinas)
        }
    }

    // Función llamada en la destrucción de la vista del Fragment
    // Libera la referencia del binding para evitar problemas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

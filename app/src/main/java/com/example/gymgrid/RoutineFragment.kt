package com.example.gymgrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gymgrid.database.AppDatabase
import com.example.gymgrid.databinding.RoutineFragmentBinding

class RoutineFragment : Fragment() {

    private var _binding: RoutineFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RoutineFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvEjercicios.layoutManager = LinearLayoutManager(context)
        cargarEjercicios()
    }

    private fun cargarEjercicios() {
        val db = AppDatabase.getDatabase(requireContext())
        db.gymDao().obtenerTodosLosEjercicios().observe(viewLifecycleOwner) { ejercicios ->
            binding.rvEjercicios.adapter = EjercicioAdapter(ejercicios, requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


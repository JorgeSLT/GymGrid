package com.example.gymgrid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.databinding.EjercicioItemBinding

class EjercicioAdapter(private val ejercicios: List<Ejercicio>) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val binding = EjercicioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EjercicioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        with(holder.binding) {
            val ejercicio = ejercicios[position]
            tvTituloEjercicio.text = ejercicio.titulo
            tvDescripcionEjercicio.text = ejercicio.descripcion
        }
    }

    override fun getItemCount(): Int = ejercicios.size

    class EjercicioViewHolder(val binding: EjercicioItemBinding) : RecyclerView.ViewHolder(binding.root)
}

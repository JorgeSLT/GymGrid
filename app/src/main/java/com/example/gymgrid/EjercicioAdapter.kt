package com.example.gymgrid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.databinding.EjercicioItemBinding

class EjercicioAdapter(private val ejercicios: List<Ejercicio>, private val context: Context) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val binding = EjercicioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EjercicioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        with(holder.binding) {
            val ejercicio = ejercicios[position]
            tvTituloEjercicio.text = ejercicio.titulo
            tvDescripcionEjercicio.text = ejercicio.descripcion

            val imageId = context.resources.getIdentifier(ejercicio.imagen, "drawable", context.packageName)
            if (imageId != 0) { // Si el recurso existe
                imageViewEjercicio.setImageResource(imageId)
            } else {
                // Opcional: establece una imagen por defecto si el recurso no se encuentra
                imageViewEjercicio.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun getItemCount(): Int = ejercicios.size

    class EjercicioViewHolder(val binding: EjercicioItemBinding) : RecyclerView.ViewHolder(binding.root)
}

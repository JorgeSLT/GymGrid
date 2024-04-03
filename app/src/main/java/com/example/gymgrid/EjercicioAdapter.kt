package com.example.gymgrid

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.database.entities.Ejercicio
import com.example.gymgrid.databinding.EjercicioItemBinding

//Adaptador de un RecyclerView que controla una lista de ejercicios
class EjercicioAdapter(private val ejercicios: List<Ejercicio>, private val context: Context) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    //Ajusta la vista para el ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val binding = EjercicioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EjercicioViewHolder(binding)
    }

    //Asigna los datos al ViewHolder
    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        with(holder.binding) {
            val ejercicio = ejercicios[position]
            tvTituloEjercicio.text = ejercicio.titulo
            tvDescripcionEjercicio.text = ejercicio.descripcion

            val imageId = context.resources.getIdentifier(ejercicio.imagen, "drawable", context.packageName)
            if (imageId != 0) {
                imageViewEjercicio.setImageResource(imageId)
            } else {
                imageViewEjercicio.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }

    //Devuelve el tamaño de la lista de ejercicios
    override fun getItemCount(): Int = ejercicios.size

    //Enlaza el ViewHolder del ejercicio con sus valores
    class EjercicioViewHolder(val binding: EjercicioItemBinding) : RecyclerView.ViewHolder(binding.root)
}

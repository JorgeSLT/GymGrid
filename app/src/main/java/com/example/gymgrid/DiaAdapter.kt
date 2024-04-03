package com.example.gymgrid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.databinding.DiaItemBinding
import com.example.gymgrid.databinding.EjercicioItemBinding
import com.example.gymgrid.database.entities.Ejercicio

//Clase sellada para poder manejar varios tipos de items (DiaItem y EjercicioItem) de manera segura
sealed class RutinaItem {
    data class DiaItem(val nombreDia: String, val diaSemana: String) : RutinaItem()
    data class EjercicioItem(val ejercicio: Ejercicio) : RutinaItem()
}

//Adaptador de un RecyclerView que controla una lista de ejercicios
class DiaAdapter(private val items: List<RutinaItem>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Companion object para definir constantes que representan los tipos de vistas
    companion object {
        private const val TYPE_DIA = 0
        private const val TYPE_EJERCICIO = 1
    }

    //Devuelve el tipo de vista segun la posicion del item
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is RutinaItem.DiaItem -> TYPE_DIA
            is RutinaItem.EjercicioItem -> TYPE_EJERCICIO
        }
    }

    //Dependiendo del tipo de vista crea un ViewHolder ajustado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DIA -> {
                val binding = DiaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DiaViewHolder(binding)
            }
            TYPE_EJERCICIO -> {
                val binding = EjercicioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EjercicioViewHolder(binding, context)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    //Asigna todos los datos a las vistas en el ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RutinaItem.DiaItem -> (holder as DiaViewHolder).bind(item)
            is RutinaItem.EjercicioItem -> (holder as EjercicioViewHolder).bind(item)
        }
    }

    //Devuelve el numero total de items de la lista
    override fun getItemCount(): Int = items.size

    //Maneja los items de tipo Dia
    //Solo muestra el dia de la semana que es
    class DiaViewHolder(private val binding: DiaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RutinaItem.DiaItem) {
            binding.tvDiaSemana.text = item.diaSemana
        }
    }

    //Maneja los items de tipo Ejercicio
    //Muestra una imagen, el nombre y la descripcion
    class EjercicioViewHolder(private val binding: EjercicioItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RutinaItem.EjercicioItem) {
            binding.tvTituloEjercicio.text = item.ejercicio.titulo
            binding.tvDescripcionEjercicio.text = item.ejercicio.descripcion

            val imageId = context.resources.getIdentifier(item.ejercicio.imagen, "drawable", context.packageName)
            if (imageId != 0) {
                binding.imageViewEjercicio.setImageResource(imageId)
            } else {
                binding.imageViewEjercicio.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}

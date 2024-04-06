package com.example.gymgrid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.databinding.RutinaItemBinding
import com.example.gymgrid.database.entities.Rutina

//Adaptador de un RecyclerView que controla una lista de rutinas
class RutinaAdapter(var rutinas: List<Rutina> = emptyList()) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    //Dependiendo del tipo de vista crea un ViewHolder ajustado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val binding = RutinaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RutinaViewHolder(binding)
    }

    //Asigna todos los datos a las vistas en el ViewHolder
    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        with(holder.binding) {
            val rutina = rutinas[position]
            tvNombreRutina.text = rutina.nombre
            tvDescripcionRutina.text = rutina.descripcion ?: ""
        }
    }

    //Devuelve el numero total de items de la lista
    override fun getItemCount(): Int = rutinas.size

    //Maneja los items de tipo Rutina
    class RutinaViewHolder(val binding: RutinaItemBinding) : RecyclerView.ViewHolder(binding.root)
}

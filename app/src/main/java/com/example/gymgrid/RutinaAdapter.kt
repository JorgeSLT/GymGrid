package com.example.gymgrid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gymgrid.database.entities.Rutina
import com.example.gymgrid.databinding.RutinaItemBinding

//NO USADO POR AHORA
class RutinaAdapter(private var rutinas: List<Rutina> = emptyList()) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val binding = RutinaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RutinaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        with(holder.binding) {
            val rutina = rutinas[position]
            tvNombreRutina.text = rutina.nombre
            tvDescripcionRutina.text = rutina.descripcion ?: ""
        }
    }

    override fun getItemCount(): Int = rutinas.size

    fun updateRutinas(nuevasRutinas: List<Rutina>) {
        this.rutinas = nuevasRutinas
        notifyDataSetChanged()
    }

    class RutinaViewHolder(val binding: RutinaItemBinding) : RecyclerView.ViewHolder(binding.root)
}
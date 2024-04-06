package com.example.gymgrid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gymgrid.databinding.ProfileFragmentBinding
import java.util.Calendar

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Una vez creada la vista se llamada a las multiples funciones para mostrar el perfil
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserName()
        showUserGoal()
        showUserDays()

        binding.editProfileButton.setOnClickListener {
            editUserName()
        }

        binding.editGoalButton.setOnClickListener {
            editUserGoal()
        }
        binding.editDaysButton.setOnClickListener {
            editUserDays()
        }
    }

    //Obtiene el nombre de usuario desde sharedPreferences y lo muestra en la vista
    private fun showUserName() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("USER_NAME", "Usuario Anónimo") ?: "Usuario Anónimo"
        binding.profileNameText.text = userName
    }

    //Obtiene el objetivo del usuario desde sharedPreferences y lo muestra en la vista
    private fun showUserGoal() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("FITNESS_GOAL", "Perder peso") ?: "Perder peso"
        binding.profileGoalText.text = userName
    }

    //Obtiene la duracion de la rutina del usuario desde sharedPreferences y la muestra en la vista
    private fun showUserDays() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("TRAINING_DAYS", "3 días") ?: "3 días"
        binding.profileDaysText.text = userName
    }

    //Funciones para editar los distintos valores de un usuario y guardarlos de nuevo en sharedPreferences
    private fun editUserName() {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_custom_layout, null)
        val editText = view.findViewById<EditText>(R.id.editTextUserName)
        editText.setText(binding.profileNameText.text)

        AlertDialog.Builder(requireContext())
            .setTitle("Editar nombre")
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    saveUserName(newName)
                    binding.profileNameText.text = newName
                }
            }
            .create().show()
    }

    private fun saveUserName(name: String) {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("USER_NAME", name)
            putBoolean("FIRST_TIME", false)
            apply()
        }
        binding.profileNameText.text = name
    }

    private fun editUserGoal() {
        val goals = arrayOf("Perder peso", "Ganar músculo")
        val currentGoal = binding.profileGoalText.text.toString()

        val selectedGoalIndex = goals.indexOfFirst { it == currentGoal }.takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle("Editar objetivo")
            .setSingleChoiceItems(goals, selectedGoalIndex) { dialog, which ->
                val selectedGoal = goals[which]
                saveUserGoal(selectedGoal)
                binding.profileGoalText.text = selectedGoal
                dialog.dismiss()
            }
            .create().show()
    }

    private fun saveUserGoal(goal: String) {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("FITNESS_GOAL", goal)
            apply()
        }
    }

    private fun editUserDays() {
        val daysOptions = arrayOf("3 días", "5 días")
        val currentDays = binding.profileDaysText.text.toString()

        val selectedDaysIndex = daysOptions.indexOfFirst { it == currentDays }.takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle("Editar días de entrenamiento")
            .setSingleChoiceItems(daysOptions, selectedDaysIndex) { dialog, which ->
                val selectedDays = daysOptions[which]
                saveUserDays(selectedDays)
                binding.profileDaysText.text = selectedDays
                dialog.dismiss()
            }
            .create().show()
    }

    private fun saveUserDays(days: String) {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("TRAINING_DAYS", days)
            apply()
        }

        planificarNotificacionesPreferencias(days)
    }

    private fun planificarNotificacionesPreferencias(days: String) {
        //Llamada a la funcion para cancelar notificaciones programadas previamente
        cancelarNotificaciones()

        val calendario = Calendar.getInstance()
        val hoy = calendario.get(Calendar.DAY_OF_WEEK)
        val daysOfWeek = if (days.startsWith("3")) {
            listOf(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
        } else {
            listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)
        }

        daysOfWeek.forEach { dayOfWeek ->
            //Evitar programar dias pasados
            if (dayOfWeek >= hoy) {
                planificarNotificacionesDia(dayOfWeek)
            }
        }
    }

    private fun planificarNotificacionesDia(dayOfWeek: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent)
    }

    private fun cancelarNotificaciones() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        (Calendar.MONDAY..Calendar.FRIDAY).forEach { dayOfWeek ->
            val intent = Intent(requireContext(), NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(requireContext(), dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

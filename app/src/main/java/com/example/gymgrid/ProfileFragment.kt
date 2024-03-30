package com.example.gymgrid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gymgrid.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

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

    private fun showUserName() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("USER_NAME", "Usuario Anónimo") ?: "Usuario Anónimo"
        binding.profileNameText.text = userName
    }

    private fun showUserGoal() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("FITNESS_GOAL", "Perder peso") ?: "Perder peso"
        binding.profileGoalText.text = userName
    }

    private fun showUserDays() {
        val sharedPref = activity?.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("TRAINING_DAYS", "3 días") ?: "3 días"
        binding.profileDaysText.text = userName
    }

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
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

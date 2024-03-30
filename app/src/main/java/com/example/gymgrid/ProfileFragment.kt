package com.example.gymgrid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.gymgrid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gymgrid.databinding.CalendarFragmentBinding

class CalendarFragment : Fragment() {

    private var _binding: CalendarFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = System.currentTimeMillis()
        binding.calendarView.date = currentDate

        binding.calendarView.setOnDateChangeListener { _, _, _, _ ->
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
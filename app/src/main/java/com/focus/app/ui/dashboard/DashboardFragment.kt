package com.focus.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.focus.app.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.userStats.observe(viewLifecycleOwner) { stats ->
            stats?.let {
                binding.textStreak.text = "🔥 ${it.streak} day streak"
                binding.textLevel.text = "Level ${it.level} (${it.xp} XP)"
            }
        }

        viewModel.todayFocusMinutes.observe(viewLifecycleOwner) { minutes ->
            binding.textFocusTime.text = "Focus: ${minutes / 60}h ${minutes % 60}m today"
        }

        viewModel.todayBlockedCount.observe(viewLifecycleOwner) { count ->
            binding.textAppsBlocked.text = "Apps blocked: $count times today"
        }

        viewModel.emergencyMode.observe(viewLifecycleOwner) { active ->
            binding.buttonEmergencyMode.text =
                if (active) "🚨 Emergency Mode ON" else "🚨 Emergency Mode"
            binding.buttonEmergencyMode.alpha = if (active) 1.0f else 0.8f
        }
    }

    private fun setupClickListeners() {
        binding.buttonEmergencyMode.setOnClickListener {
            viewModel.toggleEmergencyMode()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

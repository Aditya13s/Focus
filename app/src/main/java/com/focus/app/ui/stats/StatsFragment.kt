package com.focus.app.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.focus.app.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userStats.observe(viewLifecycleOwner) { stats ->
            stats?.let {
                binding.textStreak.text = "🔥 Streak: ${it.streak} days"
                binding.textTotalFocus.text =
                    "Total focus: ${it.totalFocusMinutes / 60}h ${it.totalFocusMinutes % 60}m"
                binding.textLevel.text = "Level ${it.level} — ${it.xp} XP"
            }
        }

        viewModel.todayBlockedCount.observe(viewLifecycleOwner) { count ->
            binding.textBlockedToday.text = "Blocked $count times today"
        }

        viewModel.todayFocusMinutes.observe(viewLifecycleOwner) { minutes ->
            binding.textTodayFocus.text = "Today's focus: ${minutes / 60}h ${minutes % 60}m"
            val yearlyHours = (minutes / 60.0) * 365
            binding.textLifeImpact.text =
                "At this rate: ${"%.0f".format(yearlyHours)} focus hours/year 🚀"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

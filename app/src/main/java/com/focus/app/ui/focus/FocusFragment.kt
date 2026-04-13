package com.focus.app.ui.focus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.focus.app.databinding.FragmentFocusBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FocusFragment : Fragment() {

    private var _binding: FragmentFocusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FocusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFocusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.timeRemaining.observe(viewLifecycleOwner) { ms ->
            val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
            binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
        }

        viewModel.timerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FocusViewModel.TimerState.RUNNING -> {
                    binding.buttonStart.text = "Pause"
                    binding.buttonStop.isEnabled = true
                }
                FocusViewModel.TimerState.PAUSED -> {
                    binding.buttonStart.text = "Resume"
                    binding.buttonStop.isEnabled = true
                }
                FocusViewModel.TimerState.STOPPED -> {
                    binding.buttonStart.text = "Start"
                    binding.buttonStop.isEnabled = false
                }
            }
        }

        viewModel.sessionCount.observe(viewLifecycleOwner) { count ->
            binding.textSessionCount.text = "Sessions today: $count 🍅"
        }

        viewModel.isWorkSession.observe(viewLifecycleOwner) { isWork ->
            binding.textSessionType.text = if (isWork) "🎯 Focus Time" else "☕ Break Time"
        }

        viewModel.focusModeEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchFocusMode.isChecked = enabled
        }
    }

    private fun setupClickListeners() {
        binding.buttonStart.setOnClickListener {
            when (viewModel.timerState.value) {
                FocusViewModel.TimerState.RUNNING -> viewModel.pauseTimer()
                else -> viewModel.startTimer()
            }
        }

        binding.buttonStop.setOnClickListener { viewModel.stopTimer() }

        binding.switchFocusMode.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleFocusMode()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.focus.app.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.focus.app.databinding.FragmentSettingsBinding
import com.focus.app.utils.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        updatePermissionStatus()
    }

    private fun setupClickListeners() {
        binding.buttonGrantUsageStats.setOnClickListener {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        binding.buttonGrantAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        binding.buttonGrantOverlay.setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
        }
    }

    private fun updatePermissionStatus() {
        val ctx = requireContext()
        binding.textUsageStatsStatus.text =
            if (PermissionHelper.hasUsageStatsPermission(ctx))
                "✅ Usage Stats — granted"
            else
                "❌ Usage Stats — required"

        binding.textAccessibilityStatus.text =
            if (PermissionHelper.hasAccessibilityPermission(ctx))
                "✅ Accessibility Service — granted"
            else
                "❌ Accessibility Service — required"

        binding.textOverlayStatus.text =
            if (PermissionHelper.hasOverlayPermission(ctx))
                "✅ Overlay Permission — granted"
            else
                "❌ Overlay Permission — required"
    }

    override fun onResume() {
        super.onResume()
        updatePermissionStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

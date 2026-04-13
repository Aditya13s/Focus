package com.focus.app.ui.blocking

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.focus.app.data.database.entity.BlockedApp
import com.focus.app.databinding.FragmentBlockingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockingFragment : Fragment() {

    private var _binding: FragmentBlockingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BlockingViewModel by viewModels()
    private lateinit var adapter: BlockedAppsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlockingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = BlockedAppsAdapter(
            onToggle = { app -> viewModel.toggleAppBlocking(app) },
            onDelete = { app -> viewModel.removeBlockedApp(app) }
        )
        binding.recyclerBlockedApps.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerBlockedApps.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.blockedApps.observe(viewLifecycleOwner) { apps ->
            adapter.submitList(apps)
            binding.textEmptyState.visibility = if (apps.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.fabAddApp.setOnClickListener { showAddAppDialog() }
    }

    private fun showAddAppDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "e.g. com.instagram.android"
            setPadding(48, 24, 48, 24)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Block an App")
            .setMessage("Enter the package name of the app to block")
            .setView(editText)
            .setPositiveButton("Block") { _, _ ->
                val pkg = editText.text.toString().trim()
                if (pkg.isNotEmpty()) {
                    val appName = getInstalledAppName(pkg)
                    if (appName == null) {
                        Toast.makeText(
                            requireContext(),
                            "App not installed as package appears to be invalid",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viewModel.addBlockedApp(BlockedApp(packageName = pkg, appName = appName))
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getInstalledAppName(packageName: String): String? {
        return try {
            val appInfo = requireContext().packageManager.getApplicationInfo(packageName, 0)
            requireContext().packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.focus.app.ui.overlay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.focus.app.databinding.ActivityReplacementSuggestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplacementSuggestionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReplacementSuggestionBinding

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplacementSuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.buttonLearn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wikipedia.org")))
            finish()
        }

        binding.buttonNotes.setOnClickListener {
            // No standard category for notes apps; fall back to home
            goHome()
            finish()
        }

        binding.buttonBreathe.setOnClickListener {
            // Breathe / meditate — just close and go home
            goHome()
            finish()
        }

        binding.buttonProceed.setOnClickListener {
            // User insists — let them through but record it
            finish()
        }

        binding.buttonClose.setOnClickListener {
            goHome()
            finish()
        }
    }

    private fun goHome() {
        startActivity(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}

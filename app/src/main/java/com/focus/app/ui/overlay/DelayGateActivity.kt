package com.focus.app.ui.overlay

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.focus.app.data.database.entity.UsageLog
import com.focus.app.data.repository.BlockingRepository
import com.focus.app.data.repository.StatsRepository
import com.focus.app.databinding.ActivityDelayGateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DelayGateActivity : AppCompatActivity() {

    @Inject
    lateinit var blockingRepository: BlockingRepository

    @Inject
    lateinit var statsRepository: StatsRepository

    private lateinit var binding: ActivityDelayGateBinding
    private var targetPackage: String = ""
    private var selectedReason: String = ""
    private var countDownTimer: CountDownTimer? = null

    companion object {
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
        private const val COUNTDOWN_SECONDS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDelayGateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetPackage = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: run { finish(); return }
        setupUI()
        startCountdown()
    }

    private fun setupUI() {
        binding.textTitle.text = "Why are you opening ${getAppName(targetPackage)}?"
        binding.textPackageName.text = targetPackage

        binding.buttonBored.setOnClickListener { selectReason("BORED") }
        binding.buttonHabit.setOnClickListener { selectReason("HABIT") }
        binding.buttonIntentional.setOnClickListener { selectReason("INTENTIONAL") }
        binding.buttonResist.setOnClickListener { resistAndClose() }
    }

    private fun selectReason(reason: String) {
        selectedReason = reason
        binding.buttonBored.alpha = if (reason == "BORED") 1.0f else 0.5f
        binding.buttonHabit.alpha = if (reason == "HABIT") 1.0f else 0.5f
        binding.buttonIntentional.alpha = if (reason == "INTENTIONAL") 1.0f else 0.5f
    }

    private fun startCountdown() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(COUNTDOWN_SECONDS * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                binding.textCountdown.text = "$secondsLeft"
                binding.progressCountdown.progress =
                    ((COUNTDOWN_SECONDS - secondsLeft) * 100 / COUNTDOWN_SECONDS)
            }

            override fun onFinish() {
                binding.textCountdown.text = "0"
                binding.progressCountdown.progress = 100
                proceedOrSuggest()
            }
        }.start()
    }

    private fun proceedOrSuggest() {
        lifecycleScope.launch {
            statsRepository.logAppOpen(
                UsageLog(
                    packageName = targetPackage,
                    reason = selectedReason,
                    wasBlocked = true,
                    wasResisted = false
                )
            )
            blockingRepository.incrementOpenCount(targetPackage)
        }

        if (selectedReason == "BORED" || selectedReason == "HABIT") {
            startActivity(
                Intent(this, ReplacementSuggestionActivity::class.java).apply {
                    putExtra(ReplacementSuggestionActivity.EXTRA_PACKAGE_NAME, targetPackage)
                }
            )
        }
        finish()
    }

    private fun resistAndClose() {
        countDownTimer?.cancel()
        lifecycleScope.launch {
            statsRepository.logAppOpen(
                UsageLog(
                    packageName = targetPackage,
                    reason = selectedReason,
                    wasBlocked = true,
                    wasResisted = true
                )
            )
            statsRepository.awardXpForResisting()
        }
        goHome()
        finish()
    }

    private fun goHome() {
        startActivity(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    private fun getAppName(pkg: String): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(pkg, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            pkg.substringAfterLast('.').replaceFirstChar { it.uppercase() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}

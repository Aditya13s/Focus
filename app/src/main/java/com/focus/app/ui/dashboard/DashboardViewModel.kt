package com.focus.app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.focus.app.data.database.entity.UserStats
import com.focus.app.data.repository.BlockingRepository
import com.focus.app.data.repository.FocusRepository
import com.focus.app.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val focusRepository: FocusRepository,
    private val statsRepository: StatsRepository,
    private val blockingRepository: BlockingRepository
) : ViewModel() {

    val userStats: LiveData<UserStats?> = focusRepository.getUserStats().asLiveData()

    private val _todayFocusMinutes = MutableLiveData(0)
    val todayFocusMinutes: LiveData<Int> = _todayFocusMinutes

    private val _todayBlockedCount = MutableLiveData(0)
    val todayBlockedCount: LiveData<Int> = _todayBlockedCount

    private val _emergencyMode = MutableLiveData(false)
    val emergencyMode: LiveData<Boolean> = _emergencyMode

    init {
        viewModelScope.launch { focusRepository.ensureUserStatsExist() }
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _todayFocusMinutes.value = focusRepository.getTodayFocusMinutes()
            _todayBlockedCount.value = statsRepository.getTodayBlockedCount()
        }
    }

    fun toggleEmergencyMode() {
        _emergencyMode.value = !(_emergencyMode.value ?: false)
    }
}

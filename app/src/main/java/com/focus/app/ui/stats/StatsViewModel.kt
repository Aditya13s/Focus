package com.focus.app.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.focus.app.data.database.entity.UsageLog
import com.focus.app.data.database.entity.UserStats
import com.focus.app.data.repository.FocusRepository
import com.focus.app.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository,
    private val focusRepository: FocusRepository
) : ViewModel() {

    val userStats: LiveData<UserStats?> = statsRepository.getUserStats().asLiveData()
    val allLogs: LiveData<List<UsageLog>> = statsRepository.getAllLogs().asLiveData()

    private val _todayBlockedCount = MutableLiveData(0)
    val todayBlockedCount: LiveData<Int> = _todayBlockedCount

    private val _todayFocusMinutes = MutableLiveData(0)
    val todayFocusMinutes: LiveData<Int> = _todayFocusMinutes

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _todayBlockedCount.value = statsRepository.getTodayBlockedCount()
            _todayFocusMinutes.value = focusRepository.getTodayFocusMinutes()
        }
    }
}

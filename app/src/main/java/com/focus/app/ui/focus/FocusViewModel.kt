package com.focus.app.ui.focus

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focus.app.data.database.entity.FocusSession
import com.focus.app.data.repository.FocusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val focusRepository: FocusRepository
) : ViewModel() {

    enum class TimerState { STOPPED, RUNNING, PAUSED }

    private val workDurationMs = 25 * 60 * 1000L
    private val breakDurationMs = 5 * 60 * 1000L

    private val _timerState = MutableLiveData(TimerState.STOPPED)
    val timerState: LiveData<TimerState> = _timerState

    private val _timeRemaining = MutableLiveData(workDurationMs)
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _sessionCount = MutableLiveData(0)
    val sessionCount: LiveData<Int> = _sessionCount

    private val _isWorkSession = MutableLiveData(true)
    val isWorkSession: LiveData<Boolean> = _isWorkSession

    private val _focusModeEnabled = MutableLiveData(false)
    val focusModeEnabled: LiveData<Boolean> = _focusModeEnabled

    private var countDownTimer: CountDownTimer? = null
    private var currentSessionId: Long = -1
    private var sessionStartTime: Long = 0

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return

        val duration = if (_isWorkSession.value == true) workDurationMs else breakDurationMs
        val remaining = _timeRemaining.value ?: duration

        if (_timerState.value == TimerState.STOPPED) {
            sessionStartTime = System.currentTimeMillis()
            viewModelScope.launch {
                currentSessionId = focusRepository.startSession(
                    FocusSession(
                        startTime = sessionStartTime,
                        type = if (_isWorkSession.value == true) "WORK" else "BREAK",
                        durationMinutes = if (_isWorkSession.value == true) 25 else 5
                    )
                )
            }
        }

        _timerState.value = TimerState.RUNNING

        countDownTimer = object : CountDownTimer(remaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = millisUntilFinished
            }
            override fun onFinish() {
                onTimerFinished()
            }
        }.start()
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        _timerState.value = TimerState.STOPPED
        _timeRemaining.value = if (_isWorkSession.value == true) workDurationMs else breakDurationMs
    }

    private fun onTimerFinished() {
        _timerState.value = TimerState.STOPPED

        if (_isWorkSession.value == true) {
            viewModelScope.launch {
                focusRepository.completeSession(
                    FocusSession(
                        id = currentSessionId,
                        startTime = sessionStartTime,
                        endTime = System.currentTimeMillis(),
                        type = "WORK",
                        completed = true,
                        durationMinutes = 25
                    )
                )
            }
            _sessionCount.value = (_sessionCount.value ?: 0) + 1
        }

        _isWorkSession.value = !(_isWorkSession.value ?: true)
        _timeRemaining.value = if (_isWorkSession.value == true) workDurationMs else breakDurationMs
    }

    fun toggleFocusMode() {
        _focusModeEnabled.value = !(_focusModeEnabled.value ?: false)
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}

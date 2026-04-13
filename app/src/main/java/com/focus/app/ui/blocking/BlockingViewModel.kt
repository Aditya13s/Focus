package com.focus.app.ui.blocking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.focus.app.data.database.entity.BlockedApp
import com.focus.app.data.repository.BlockingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockingViewModel @Inject constructor(
    private val blockingRepository: BlockingRepository
) : ViewModel() {

    val blockedApps: LiveData<List<BlockedApp>> =
        blockingRepository.getAllBlockedApps().asLiveData()

    fun addBlockedApp(app: BlockedApp) {
        viewModelScope.launch { blockingRepository.addBlockedApp(app) }
    }

    fun removeBlockedApp(app: BlockedApp) {
        viewModelScope.launch { blockingRepository.removeBlockedApp(app) }
    }

    fun toggleAppBlocking(app: BlockedApp) {
        viewModelScope.launch {
            blockingRepository.updateBlockedApp(app.copy(isEnabled = !app.isEnabled))
        }
    }

    fun updateQuota(app: BlockedApp, quota: Int) {
        viewModelScope.launch {
            blockingRepository.updateBlockedApp(app.copy(dailyQuota = quota))
        }
    }
}

package kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kozakiewicz.szymon.dragtimer.common.viewModel.BaseScreenAndNavEffectsViewModel
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.notifications.scheduler.NotificationScheduler
import javax.inject.Inject

@HiltViewModel
class IntervalTrackerViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    private val notificationScheduler: NotificationScheduler
) : BaseScreenAndNavEffectsViewModel<IntervalTrackerEffect, IntervalTrackerNavEffect>() {

    private val _state = MutableStateFlow(IntervalTrackerState(isLoading = true))
    val state: StateFlow<IntervalTrackerState> = _state.asStateFlow()

    init {
        observeDrugs()
        startLiveTimer()
    }

    fun onEvent(event: IntervalTrackerEvent) {
        when (event) {
            is IntervalTrackerEvent.OnAddDrugClicked -> {
                sendNavEffect(IntervalTrackerNavEffect.NavigateToAddDrug)
            }
            is IntervalTrackerEvent.OnDrugCardClicked -> {
                sendNavEffect(IntervalTrackerNavEffect.NavigateToDrugDetails(event.drugId))
            }
            is IntervalTrackerEvent.OnTakeDrugClicked -> {
                takeDrug(event.drugId)
            }
        }
    }

    private var timerJob: kotlinx.coroutines.Job? = null

    private fun observeDrugs() {
        viewModelScope.launch {
            drugRepository.getAllDrugs().collect { drugList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        drugs = drugList
                    )
                }
            }
        }
    }

    fun startLiveTimer() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _state.update { it.copy(currentTimeMillis = System.currentTimeMillis()) }
            }
        }
    }

    fun stopLiveTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopLiveTimer()
    }

    private fun takeDrug(drugId: Int) {
        viewModelScope.launch {
            val drug = _state.value.drugs.find { it.id == drugId } ?: return@launch
            val now = System.currentTimeMillis()
            val updatedDrug = drug.copy(dragTime = now)

            drugRepository.updateDrug(updatedDrug)
            drugRepository.recordDrugTaken(drugId, now)
            scheduleNextAlarm(updatedDrug, now)

            sendEffect(IntervalTrackerEffect.ShowSnackbar(drug.name))
        }
    }

    private fun scheduleNextAlarm(drug: Drug, takenTime: Long) {
        val nextTriggerTime = takenTime + drug.timeInterval
        notificationScheduler.scheduleDoseAlarm(drug.id, drug.name, nextTriggerTime)
    }
}

package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation.MedicationManagementScreen
import kozakiewicz.szymon.dragtimer.feature.notifications.scheduler.NotificationScheduler
import javax.inject.Inject

@HiltViewModel
class MedicationDetailsViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : BaseScreenAndNavEffectsViewModel<MedicationDetailsEffect, MedicationDetailsNavEffect>() {

    private val navRoute: MedicationManagementScreen.Details = savedStateHandle.toRoute()
    private val _state = MutableStateFlow(MedicationDetailsState(drugId = navRoute.drugId))
    val state: StateFlow<MedicationDetailsState> = _state.asStateFlow()

    init {
        observeDrug(navRoute.drugId)
        startLiveTimer()
    }

    fun onEvent(event: MedicationDetailsEvent) {
        when (event) {
            is MedicationDetailsEvent.OnTakeDrugClicked -> takeDrug()
            is MedicationDetailsEvent.OnResetTimerClicked -> resetTimer()
            is MedicationDetailsEvent.OnEditDrugClicked -> {
                sendNavEffect(MedicationDetailsNavEffect.NavigateToEdit(_state.value.drugId))
            }
            is MedicationDetailsEvent.OnDeleteDrugClicked -> {
                _state.update { it.copy(showDeleteConfirmDialog = true) }
            }
            is MedicationDetailsEvent.OnConfirmDeleteClicked -> deleteDrug()
            is MedicationDetailsEvent.OnDismissDeleteDialog -> {
                _state.update { it.copy(showDeleteConfirmDialog = false) }
            }
            is MedicationDetailsEvent.OnViewStatsClicked -> {
                sendNavEffect(MedicationDetailsNavEffect.NavigateToStats(_state.value.drugId))
            }
            is MedicationDetailsEvent.OnBackClicked -> {
                sendNavEffect(MedicationDetailsNavEffect.NavigateBack)
            }
        }
    }

    private fun observeDrug(drugId: Int) {
        viewModelScope.launch {
            drugRepository.getDrugById(drugId).collect { drug ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        drug = drug
                    )
                }
            }
        }
    }

    private var timerJob: kotlinx.coroutines.Job? = null

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

    private fun takeDrug() {
        val currentDrug = _state.value.drug ?: return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val updated = currentDrug.copy(dragTime = now)
            drugRepository.updateDrug(updated)
            drugRepository.recordDrugTaken(currentDrug.id, now)

            val nextTriggerTime = now + currentDrug.timeInterval
            notificationScheduler.scheduleDoseAlarm(currentDrug.id, currentDrug.name, nextTriggerTime)

            sendEffect(MedicationDetailsEffect.ShowSnackbar(currentDrug.name))
        }
    }

    private fun resetTimer() {
        val currentDrug = _state.value.drug ?: return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val updated = currentDrug.copy(dragTime = now - currentDrug.timeInterval)
            drugRepository.updateDrug(updated)
            sendEffect(MedicationDetailsEffect.ShowSnackbar(currentDrug.name))
        }
    }

    private fun deleteDrug() {
        val currentDrug = _state.value.drug ?: return
        viewModelScope.launch {
            notificationScheduler.cancelDoseAlarm(currentDrug.id, currentDrug.name)
            drugRepository.deleteDrug(currentDrug)
            _state.update { it.copy(showDeleteConfirmDialog = false) }
            sendNavEffect(MedicationDetailsNavEffect.NavigateBack)
        }
    }
}

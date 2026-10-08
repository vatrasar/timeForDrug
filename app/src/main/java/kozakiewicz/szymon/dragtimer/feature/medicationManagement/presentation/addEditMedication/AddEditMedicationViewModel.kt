package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kozakiewicz.szymon.dragtimer.common.util.TimeUtils
import kozakiewicz.szymon.dragtimer.common.viewModel.BaseScreenAndNavEffectsViewModel
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation.MedicationManagementScreen
import kozakiewicz.szymon.dragtimer.feature.notifications.scheduler.NotificationScheduler
import javax.inject.Inject

@HiltViewModel
class AddEditMedicationViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    private val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : BaseScreenAndNavEffectsViewModel<AddEditMedicationEffect, AddEditMedicationNavEffect>() {

    private val navRoute: MedicationManagementScreen.AddEdit = savedStateHandle.toRoute()
    private val _state = MutableStateFlow(AddEditMedicationState(drugId = navRoute.drugId))
    val state: StateFlow<AddEditMedicationState> = _state.asStateFlow()

    init {
        loadDrugIfEditing(navRoute.drugId)
    }

    fun onEvent(event: AddEditMedicationEvent) {
        when (event) {
            is AddEditMedicationEvent.OnNameChanged -> updateName(event.value)
            is AddEditMedicationEvent.OnHoursChanged -> updateHours(event.value)
            is AddEditMedicationEvent.OnMinutesChanged -> updateMinutes(event.value)
            is AddEditMedicationEvent.OnSecondsChanged -> updateSeconds(event.value)
            is AddEditMedicationEvent.OnSaveClicked -> saveMedication()
            is AddEditMedicationEvent.OnBackClicked -> sendNavEffect(AddEditMedicationNavEffect.NavigateBack)
        }
    }

    private fun loadDrugIfEditing(drugId: Int) {
        if (drugId <= 0) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val drug = drugRepository.getDrugByIdDirect(drugId) ?: return@launch
            _state.update {
                it.copy(
                    isLoading = false,
                    isEditing = true,
                    name = drug.name,
                    hours = TimeUtils.millisToHours(drug.timeInterval).toString(),
                    minutes = TimeUtils.millisToMinutes(drug.timeInterval).toString(),
                    seconds = TimeUtils.millisToSeconds(drug.timeInterval).toString()
                )
            }
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name, nameError = null) }
    }

    private fun updateHours(hours: String) {
        _state.update { it.copy(hours = hours.filter { char -> char.isDigit() }, intervalError = null) }
    }

    private fun updateMinutes(minutes: String) {
        _state.update { it.copy(minutes = minutes.filter { char -> char.isDigit() }, intervalError = null) }
    }

    private fun updateSeconds(seconds: String) {
        _state.update { it.copy(seconds = seconds.filter { char -> char.isDigit() }, intervalError = null) }
    }

    private fun saveMedication() {
        val currentState = _state.value
        val nameTrimmed = currentState.name.trim()
        if (nameTrimmed.isEmpty()) {
            _state.update { it.copy(nameError = "Wpisz nazwę leku") }
            return
        }

        val hours = currentState.hours.toIntOrNull() ?: 0
        val minutes = currentState.minutes.toIntOrNull() ?: 0
        val seconds = currentState.seconds.toIntOrNull() ?: 0
        val intervalMillis = TimeUtils.toMillis(hours, minutes, seconds)

        if (intervalMillis <= 0L) {
            _state.update { it.copy(intervalError = "Interwał musi być większy od zera") }
            return
        }

        viewModelScope.launch {
            if (currentState.isEditing) {
                val existing = drugRepository.getDrugByIdDirect(currentState.drugId)
                if (existing != null) {
                    val updated = existing.copy(name = nameTrimmed, timeInterval = intervalMillis)
                    drugRepository.updateDrug(updated)
                }
            } else {
                val now = System.currentTimeMillis()
                val newDrug = Drug(
                    name = nameTrimmed,
                    timeInterval = intervalMillis,
                    dragTime = now - intervalMillis
                )
                drugRepository.insertDrug(newDrug)
            }
            sendNavEffect(AddEditMedicationNavEffect.NavigateBack)
        }
    }
}

package kozakiewicz.szymon.dragtimer.feature.doseHistory.presentation.doseHistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kozakiewicz.szymon.dragtimer.common.viewModel.BaseScreenAndNavEffectsViewModel
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.doseHistory.navigation.DoseHistoryScreen
import javax.inject.Inject

@HiltViewModel
class DoseHistoryViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    savedStateHandle: SavedStateHandle
) : BaseScreenAndNavEffectsViewModel<DoseHistoryEffect, DoseHistoryNavEffect>() {

    private val navRoute: DoseHistoryScreen.History = savedStateHandle.toRoute()
    private val _state = MutableStateFlow(DoseHistoryState(selectedDrugId = navRoute.drugId))
    val state: StateFlow<DoseHistoryState> = _state.asStateFlow()

    init {
        observeHistory()
    }

    fun onEvent(event: DoseHistoryEvent) {
        when (event) {
            is DoseHistoryEvent.OnFilterDrugSelected -> updateSelectedFilter(event.drugId)
        }
    }

    private fun updateSelectedFilter(drugId: Int) {
        _state.update { it.copy(selectedDrugId = drugId) }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            combine(
                drugRepository.getAllDrugs(),
                drugRepository.getAllDoseEvents(),
                _state
            ) { drugs, events, currentState ->
                val items = buildHistoryItems(drugs, events, currentState.selectedDrugId)
                Triple(drugs, items, currentState.selectedDrugId)
            }.collect { (drugs, items, _) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        drugs = drugs,
                        events = items
                    )
                }
            }
        }
    }

    private fun buildHistoryItems(
        drugs: List<Drug>,
        events: List<TakeDrugEvent>,
        selectedDrugId: Int
    ): List<DoseHistoryItem> {
        val drugNameMap = drugs.associate { it.id to it.name }
        return events
            .filter { selectedDrugId <= 0 || it.drugId == selectedDrugId }
            .map { event ->
                DoseHistoryItem(
                    id = event.idDrugEvent,
                    drugName = drugNameMap[event.drugId] ?: "Lek #${event.drugId}",
                    timestamp = event.drugTime
                )
            }
    }
}

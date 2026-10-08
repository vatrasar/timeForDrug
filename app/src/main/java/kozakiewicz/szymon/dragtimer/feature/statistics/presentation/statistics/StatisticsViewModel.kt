package kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics

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
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.statistics.navigation.StatisticsScreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val drugRepository: DrugRepository,
    savedStateHandle: SavedStateHandle
) : BaseScreenAndNavEffectsViewModel<StatisticsEffect, StatisticsNavEffect>() {

    private val navRoute: StatisticsScreen.Stats = savedStateHandle.toRoute()
    private val _state = MutableStateFlow(StatisticsState(selectedDrugId = navRoute.drugId))
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        observeData()
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnFilterDrugSelected -> updateSelectedFilter(event.drugId)
        }
    }

    private fun updateSelectedFilter(drugId: Int) {
        _state.update { it.copy(selectedDrugId = drugId) }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                drugRepository.getAllDrugs(),
                drugRepository.getAllDoseEvents(),
                _state
            ) { drugs, events, currentState ->
                val stats = computeWeeklyStats(events, currentState.selectedDrugId)
                Pair(drugs, stats)
            }.collect { (drugs, stats) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        drugs = drugs,
                        dailyDoseCounts = stats
                    )
                }
            }
        }
    }

    private fun computeWeeklyStats(
        events: List<TakeDrugEvent>,
        selectedDrugId: Int
    ): List<DayDoseStat> {
        val filteredEvents = events.filter { selectedDrugId <= 0 || it.drugId == selectedDrugId }
        val daySlots = generateLast7Days()
        return daySlots.map { (dayLabel, startMillis, endMillis) ->
            val count = filteredEvents.count { it.drugTime in startMillis..endMillis }
            DayDoseStat(
                dayLabel = dayLabel,
                count = count,
                dateMillis = startMillis
            )
        }
    }

    private fun generateLast7Days(): List<Triple<String, Long, Long>> {
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val result = mutableListOf<Triple<String, Long, Long>>()
        val calendar = Calendar.getInstance()

        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = calendar.timeInMillis
            cal.add(Calendar.DAY_OF_YEAR, -i)

            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startMillis = cal.timeInMillis

            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
            val endMillis = cal.timeInMillis

            val label = dayFormat.format(cal.time)
            result.add(Triple(label, startMillis, endMillis))
        }
        return result
    }
}

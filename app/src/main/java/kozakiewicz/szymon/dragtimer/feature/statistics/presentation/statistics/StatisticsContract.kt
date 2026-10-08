package kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics

import androidx.compose.runtime.Immutable
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

@Immutable
data class DayDoseStat(
    val dayLabel: String,
    val count: Int,
    val dateMillis: Long
)

sealed class StatisticsEffect

sealed class StatisticsNavEffect

sealed class StatisticsEvent {
    data class OnFilterDrugSelected(val drugId: Int) : StatisticsEvent()
}

@Immutable
data class StatisticsState(
    val isLoading: Boolean = true,
    val selectedDrugId: Int = -1,
    val drugs: List<Drug> = emptyList(),
    val dailyDoseCounts: List<DayDoseStat> = emptyList()
)

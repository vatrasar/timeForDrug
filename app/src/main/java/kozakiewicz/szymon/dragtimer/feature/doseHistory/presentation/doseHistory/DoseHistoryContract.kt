package kozakiewicz.szymon.dragtimer.feature.doseHistory.presentation.doseHistory

import androidx.compose.runtime.Immutable
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

@Immutable
data class DoseHistoryItem(
    val id: Int,
    val drugName: String,
    val timestamp: Long
)

sealed class DoseHistoryEffect

sealed class DoseHistoryNavEffect

sealed class DoseHistoryEvent {
    data class OnFilterDrugSelected(val drugId: Int) : DoseHistoryEvent()
}

@Immutable
data class DoseHistoryState(
    val isLoading: Boolean = true,
    val selectedDrugId: Int = -1,
    val drugs: List<Drug> = emptyList(),
    val events: List<DoseHistoryItem> = emptyList()
)

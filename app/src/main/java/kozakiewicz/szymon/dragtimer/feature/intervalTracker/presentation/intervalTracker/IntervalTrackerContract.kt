package kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker

import androidx.compose.runtime.Immutable
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

sealed class IntervalTrackerEffect {
    data class ShowSnackbar(val message: String) : IntervalTrackerEffect()
}

sealed class IntervalTrackerNavEffect {
    data object NavigateToAddDrug : IntervalTrackerNavEffect()
    data class NavigateToDrugDetails(val drugId: Int) : IntervalTrackerNavEffect()
}

sealed class IntervalTrackerEvent {
    data object OnAddDrugClicked : IntervalTrackerEvent()
    data class OnDrugCardClicked(val drugId: Int) : IntervalTrackerEvent()
    data class OnTakeDrugClicked(val drugId: Int) : IntervalTrackerEvent()
}

@Immutable
data class IntervalTrackerState(
    val isLoading: Boolean = false,
    val drugs: List<Drug> = emptyList(),
    val currentTimeMillis: Long = System.currentTimeMillis()
)

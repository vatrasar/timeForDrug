package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails

import androidx.compose.runtime.Immutable
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

sealed class MedicationDetailsEffect {
    data class ShowSnackbar(val message: String) : MedicationDetailsEffect()
}

sealed class MedicationDetailsNavEffect {
    data object NavigateBack : MedicationDetailsNavEffect()
    data class NavigateToEdit(val drugId: Int) : MedicationDetailsNavEffect()
    data class NavigateToStats(val drugId: Int) : MedicationDetailsNavEffect()
}

sealed class MedicationDetailsEvent {
    data object OnTakeDrugClicked : MedicationDetailsEvent()
    data object OnResetTimerClicked : MedicationDetailsEvent()
    data object OnEditDrugClicked : MedicationDetailsEvent()
    data object OnDeleteDrugClicked : MedicationDetailsEvent()
    data object OnConfirmDeleteClicked : MedicationDetailsEvent()
    data object OnDismissDeleteDialog : MedicationDetailsEvent()
    data object OnViewStatsClicked : MedicationDetailsEvent()
    data object OnBackClicked : MedicationDetailsEvent()
}

@Immutable
data class MedicationDetailsState(
    val drugId: Int = -1,
    val drug: Drug? = null,
    val isLoading: Boolean = true,
    val currentTimeMillis: Long = System.currentTimeMillis(),
    val showDeleteConfirmDialog: Boolean = false
)

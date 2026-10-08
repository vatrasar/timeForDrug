package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication

import androidx.compose.runtime.Immutable

sealed class AddEditMedicationEffect {
    data class ShowSnackbar(val message: String) : AddEditMedicationEffect()
}

sealed class AddEditMedicationNavEffect {
    data object NavigateBack : AddEditMedicationNavEffect()
}

sealed class AddEditMedicationEvent {
    data class OnNameChanged(val value: String) : AddEditMedicationEvent()
    data class OnHoursChanged(val value: String) : AddEditMedicationEvent()
    data class OnMinutesChanged(val value: String) : AddEditMedicationEvent()
    data class OnSecondsChanged(val value: String) : AddEditMedicationEvent()
    data object OnSaveClicked : AddEditMedicationEvent()
    data object OnBackClicked : AddEditMedicationEvent()
}

@Immutable
data class AddEditMedicationState(
    val drugId: Int = -1,
    val name: String = "",
    val hours: String = "0",
    val minutes: String = "0",
    val seconds: String = "0",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val intervalError: String? = null
)

package kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface MedicationManagementScreen {

    @Serializable
    data class Details(val drugId: Int) : MedicationManagementScreen

    @Serializable
    data class AddEdit(val drugId: Int = -1) : MedicationManagementScreen
}

package kozakiewicz.szymon.dragtimer.feature.doseHistory.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface DoseHistoryScreen {

    @Serializable
    data class History(val drugId: Int = -1) : DoseHistoryScreen
}

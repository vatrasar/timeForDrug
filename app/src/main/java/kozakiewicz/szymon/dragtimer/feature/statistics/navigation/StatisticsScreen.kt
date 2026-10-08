package kozakiewicz.szymon.dragtimer.feature.statistics.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface StatisticsScreen {

    @Serializable
    data class Stats(val drugId: Int = -1) : StatisticsScreen
}

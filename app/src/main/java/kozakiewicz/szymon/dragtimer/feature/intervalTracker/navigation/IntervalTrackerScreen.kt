package kozakiewicz.szymon.dragtimer.feature.intervalTracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface IntervalTrackerScreen {
    @Serializable
    data object Tracker : IntervalTrackerScreen
}

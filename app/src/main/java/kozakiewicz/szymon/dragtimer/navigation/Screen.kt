package kozakiewicz.szymon.dragtimer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.feature.doseHistory.navigation.DoseHistoryScreen
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.navigation.IntervalTrackerScreen
import kozakiewicz.szymon.dragtimer.feature.statistics.navigation.StatisticsScreen

sealed class Screen(
    val route: Any,
    val titleRes: Int,
    val icon: ImageVector,
    val testTag: String
) {
    data object Tracker : Screen(
        route = IntervalTrackerScreen.Tracker,
        titleRes = R.string.nav_tracker,
        icon = Icons.Default.Timer,
        testTag = "bottomNavTracker"
    )

    data object History : Screen(
        route = DoseHistoryScreen.History(),
        titleRes = R.string.nav_history,
        icon = Icons.Default.History,
        testTag = "bottomNavHistory"
    )

    data object Stats : Screen(
        route = StatisticsScreen.Stats(),
        titleRes = R.string.nav_statistics,
        icon = Icons.Default.BarChart,
        testTag = "bottomNavStats"
    )
}

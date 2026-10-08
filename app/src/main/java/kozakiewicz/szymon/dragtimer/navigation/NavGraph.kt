package kozakiewicz.szymon.dragtimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kozakiewicz.szymon.dragtimer.feature.doseHistory.navigation.setupDoseHistoryNavigation
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.navigation.IntervalTrackerScreen
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.navigation.setupIntervalTrackerNavigation
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation.setupMedicationManagementNavigation
import kozakiewicz.szymon.dragtimer.feature.statistics.navigation.setupStatisticsNavigation

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = IntervalTrackerScreen.Tracker,
        modifier = modifier
    ) {
        setupIntervalTrackerNavigation(navController)
        setupMedicationManagementNavigation(navController)
        setupDoseHistoryNavigation(navController)
        setupStatisticsNavigation(navController)
    }
}

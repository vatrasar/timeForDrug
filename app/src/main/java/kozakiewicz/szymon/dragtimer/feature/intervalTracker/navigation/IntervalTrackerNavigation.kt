package kozakiewicz.szymon.dragtimer.feature.intervalTracker.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerNavEffect
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerScreen
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerViewModel
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation.MedicationManagementScreen

fun NavGraphBuilder.setupIntervalTrackerNavigation(navController: NavController) {
    composable<IntervalTrackerScreen.Tracker> {
        val viewModel: IntervalTrackerViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.navEffect.collect { navEffect ->
                when (navEffect) {
                    is IntervalTrackerNavEffect.NavigateToAddDrug -> {
                        navController.navigate(MedicationManagementScreen.AddEdit())
                    }
                    is IntervalTrackerNavEffect.NavigateToDrugDetails -> {
                        navController.navigate(MedicationManagementScreen.Details(navEffect.drugId))
                    }
                }
            }
        }

        IntervalTrackerScreen(
            state = state,
            onEventFromViewModel = viewModel::onEvent,
            effectFromViewModel = viewModel.effect
        )
    }
}

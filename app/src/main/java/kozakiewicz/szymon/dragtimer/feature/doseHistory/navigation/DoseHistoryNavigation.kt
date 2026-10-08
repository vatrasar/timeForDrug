package kozakiewicz.szymon.dragtimer.feature.doseHistory.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kozakiewicz.szymon.dragtimer.feature.doseHistory.presentation.doseHistory.DoseHistoryScreen
import kozakiewicz.szymon.dragtimer.feature.doseHistory.presentation.doseHistory.DoseHistoryViewModel

fun NavGraphBuilder.setupDoseHistoryNavigation(navController: NavController) {
    composable<DoseHistoryScreen.History> {
        val viewModel: DoseHistoryViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DoseHistoryScreen(
            state = state,
            onEventFromViewModel = viewModel::onEvent,
            effectFromViewModel = viewModel.effect
        )
    }
}

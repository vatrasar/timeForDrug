package kozakiewicz.szymon.dragtimer.feature.statistics.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics.StatisticsScreen
import kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics.StatisticsViewModel

fun NavGraphBuilder.setupStatisticsNavigation(navController: NavController) {
    composable<StatisticsScreen.Stats> {
        val viewModel: StatisticsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        StatisticsScreen(
            state = state,
            onEventFromViewModel = viewModel::onEvent,
            effectFromViewModel = viewModel.effect
        )
    }
}

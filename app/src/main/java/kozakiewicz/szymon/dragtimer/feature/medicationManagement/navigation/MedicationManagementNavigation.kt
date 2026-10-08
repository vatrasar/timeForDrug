package kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationNavEffect
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationScreen
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationViewModel
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails.MedicationDetailsNavEffect
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails.MedicationDetailsScreen
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails.MedicationDetailsViewModel
import kozakiewicz.szymon.dragtimer.feature.statistics.navigation.StatisticsScreen

fun NavGraphBuilder.setupMedicationManagementNavigation(navController: NavController) {
    composable<MedicationManagementScreen.Details> {
        val viewModel: MedicationDetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.navEffect.collect { navEffect ->
                when (navEffect) {
                    is MedicationDetailsNavEffect.NavigateBack -> {
                        navController.popBackStack()
                    }
                    is MedicationDetailsNavEffect.NavigateToEdit -> {
                        navController.navigate(MedicationManagementScreen.AddEdit(navEffect.drugId))
                    }
                    is MedicationDetailsNavEffect.NavigateToStats -> {
                        navController.navigate(StatisticsScreen.Stats(navEffect.drugId))
                    }
                }
            }
        }

        MedicationDetailsScreen(
            state = state,
            onEventFromViewModel = viewModel::onEvent,
            effectFromViewModel = viewModel.effect
        )
    }

    composable<MedicationManagementScreen.AddEdit> {
        val viewModel: AddEditMedicationViewModel = hiltViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.navEffect.collect { navEffect ->
                when (navEffect) {
                    is AddEditMedicationNavEffect.NavigateBack -> {
                        navController.popBackStack()
                    }
                }
            }
        }

        AddEditMedicationScreen(
            state = state,
            onEventFromViewModel = viewModel::onEvent,
            effectFromViewModel = viewModel.effect
        )
    }
}

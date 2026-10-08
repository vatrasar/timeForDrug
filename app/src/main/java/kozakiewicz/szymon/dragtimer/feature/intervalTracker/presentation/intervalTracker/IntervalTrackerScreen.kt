package kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.ScreenComponents.IntervalTrackerCard
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.ScreenComponents.IntervalTrackerEmptyState
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.ScreenComponents.IntervalTrackerTopBar

/**
 * Main dashboard screen tracking active medications with real-time countdown timers.
 *
 * Purpose: Allows users to monitor intervals between medication doses, take doses, and navigate to details or creation.
 * Available Functionalities: View active medication countdowns, mark dose as taken, open medication details, add medication.
 * Key UI elements: IntervalTrackerTopBar, LazyColumn of IntervalTrackerCards, FloatingActionButton, SnackbarHost.
 * Navigation:
 * - Navigate From: AppBottomBar (Tracker tab).
 * - Navigate To: AddEditMedicationScreen (Add), MedicationDetailsScreen (Details).
 */
@Composable
fun IntervalTrackerScreen(
    state: IntervalTrackerState,
    onEventFromViewModel: (IntervalTrackerEvent) -> Unit,
    effectFromViewModel: Flow<IntervalTrackerEffect>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is IntervalTrackerEffect.ShowSnackbar -> {
                    val message = context.getString(R.string.drug_taken_snackbar, effect.message)
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            IntervalTrackerTopBar(activeDrugsCount = state.drugs.size)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEventFromViewModel(IntervalTrackerEvent.OnAddDrugClicked) },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.testTag("addDrugFab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_drug)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        IntervalTrackerBody(
            state = state,
            onEvent = onEventFromViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun IntervalTrackerBody(
    state: IntervalTrackerState,
    onEvent: (IntervalTrackerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.drugs.isEmpty() -> {
                IntervalTrackerEmptyState(
                    onAddDrugClicked = { onEvent(IntervalTrackerEvent.OnAddDrugClicked) }
                )
            }
            else -> {
                DrugsList(
                    drugs = state.drugs,
                    currentTimeMillis = state.currentTimeMillis,
                    onTakeDrugClicked = { onEvent(IntervalTrackerEvent.OnTakeDrugClicked(it)) },
                    onDetailsClicked = { onEvent(IntervalTrackerEvent.OnDrugCardClicked(it)) }
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.testTag("trackerLoadingIndicator")
    )
}

@Composable
private fun DrugsList(
    drugs: List<Drug>,
    currentTimeMillis: Long,
    onTakeDrugClicked: (Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("drugsLazyList")
    ) {
        items(
            items = drugs,
            key = { it.id }
        ) { drug ->
            IntervalTrackerCard(
                drug = drug,
                currentTimeMillis = currentTimeMillis,
                onTakeDrugClicked = onTakeDrugClicked,
                onDetailsClicked = onDetailsClicked
            )
        }
    }
}

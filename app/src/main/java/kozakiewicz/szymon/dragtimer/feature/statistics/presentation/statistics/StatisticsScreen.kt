package kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics.ScreenComponents.DailyIntakeBarChart

/**
 * Screen presenting visual dosage analytics and 7-day intake charts.
 *
 * Purpose: Allows the user to review adherence trends over the past seven days per medication or overall.
 * Available Functionalities: Select medication filter, inspect bar chart for daily dose counts.
 * Key UI elements: Statistics TopAppBar, Medication Filter Chips, Daily Intake Bar Chart.
 * Navigation:
 * - Navigate From: AppBottomBar (Statistics tab), MedicationDetailsScreen.
 * - Navigate To: Remains on this tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    state: StatisticsState,
    onEventFromViewModel: (StatisticsEvent) -> Unit,
    effectFromViewModel: Flow<StatisticsEffect>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.statistics_title)) },
                modifier = Modifier.testTag("statsTopBar")
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        StatisticsBody(
            state = state,
            onEvent = onEventFromViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun StatisticsBody(
    state: StatisticsState,
    onEvent: (StatisticsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                StatisticsContent(
                    state = state,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun StatisticsContent(
    state: StatisticsState,
    onEvent: (StatisticsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if (state.drugs.isNotEmpty()) {
            FilterChipsRow(
                drugs = state.drugs,
                selectedDrugId = state.selectedDrugId,
                onFilterSelected = { onEvent(StatisticsEvent.OnFilterDrugSelected(it)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.padding(16.dp)) {
            DailyIntakeBarChart(stats = state.dailyDoseCounts)
        }
    }
}

@Composable
private fun FilterChipsRow(
    drugs: List<Drug>,
    selectedDrugId: Int,
    onFilterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("statsFilterRow")
    ) {
        item {
            FilterChip(
                selected = selectedDrugId <= 0,
                onClick = { onFilterSelected(-1) },
                label = { Text(text = stringResource(id = R.string.all_drugs)) }
            )
        }
        items(
            items = drugs,
            key = { it.id }
        ) { drug ->
            FilterChip(
                selected = selectedDrugId == drug.id,
                onClick = { onFilterSelected(drug.id) },
                label = { Text(text = drug.name) }
            )
        }
    }
}

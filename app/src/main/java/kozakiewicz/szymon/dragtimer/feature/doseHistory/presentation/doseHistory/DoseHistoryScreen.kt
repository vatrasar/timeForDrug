package kozakiewicz.szymon.dragtimer.feature.doseHistory.presentation.doseHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.common.util.TimeUtils
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

/**
 * Screen presenting a chronological record of all recorded medication doses.
 *
 * Purpose: Allows users to inspect their intake timeline and filter by specific medications.
 * Available Functionalities: View chronological dose intake list, filter by medication, view intake dates and times.
 * Key UI elements: History TopAppBar, Medication Filter Chips Row, Dose Logs LazyColumn.
 * Navigation:
 * - Navigate From: AppBottomBar (History tab).
 * - Navigate To: Remains on this tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseHistoryScreen(
    state: DoseHistoryState,
    onEventFromViewModel: (DoseHistoryEvent) -> Unit,
    effectFromViewModel: Flow<DoseHistoryEffect>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.history_title)) },
                modifier = Modifier.testTag("historyTopBar")
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        DoseHistoryBody(
            state = state,
            onEvent = onEventFromViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun DoseHistoryBody(
    state: DoseHistoryState,
    onEvent: (DoseHistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (state.drugs.isNotEmpty()) {
            FilterChipsRow(
                drugs = state.drugs,
                selectedDrugId = state.selectedDrugId,
                onFilterSelected = { onEvent(DoseHistoryEvent.OnFilterDrugSelected(it)) }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.events.isEmpty() -> {
                    EmptyHistoryView(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    HistoryList(events = state.events)
                }
            }
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
            .testTag("historyFilterRow")
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

@Composable
private fun EmptyHistoryView(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(32.dp)
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.no_history),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("historyEmptyText")
        )
    }
}

@Composable
private fun HistoryList(
    events: List<DoseHistoryItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("historyLazyList")
    ) {
        items(
            items = events,
            key = { it.id }
        ) { item ->
            HistoryItemCard(item = item)
        }
    }
}

@Composable
private fun HistoryItemCard(
    item: DoseHistoryItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("historyCard_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = item.drugName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Text(
                text = TimeUtils.formatTimestamp(item.timestamp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

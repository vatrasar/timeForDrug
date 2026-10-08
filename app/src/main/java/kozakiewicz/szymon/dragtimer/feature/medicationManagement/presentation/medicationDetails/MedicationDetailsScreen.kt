package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.medicationDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kozakiewicz.szymon.dragtimer.common.util.TimeUtils
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

/**
 * Screen presenting comprehensive information, timer countdown, and control actions for a specific medication.
 *
 * Purpose: Allows the user to inspect interval details, trigger dose intake, reset timers, edit settings, or delete the medication.
 * Available Functionalities: View countdown, take medication dose, reset timer, open edit screen, delete with confirmation.
 * Key UI elements: Medication Info Card, Countdown Progress Card, Control Action Buttons, Delete Confirmation Dialog.
 * Navigation:
 * - Navigate From: IntervalTrackerScreen.
 * - Navigate To: AddEditMedicationScreen (Edit), Back to previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDetailsScreen(
    state: MedicationDetailsState,
    onEventFromViewModel: (MedicationDetailsEvent) -> Unit,
    effectFromViewModel: Flow<MedicationDetailsEffect>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is MedicationDetailsEffect.ShowSnackbar -> {
                    val message = context.getString(R.string.drug_taken_snackbar, effect.message)
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DetailsTopBar(
                title = state.drug?.name ?: stringResource(id = R.string.details),
                onBackClicked = { onEventFromViewModel(MedicationDetailsEvent.OnBackClicked) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        DetailsBody(
            state = state,
            onEvent = onEventFromViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }

    if (state.showDeleteConfirmDialog) {
        DeleteConfirmationDialog(
            onConfirm = { onEventFromViewModel(MedicationDetailsEvent.OnConfirmDeleteClicked) },
            onDismiss = { onEventFromViewModel(MedicationDetailsEvent.OnDismissDeleteDialog) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    title: String,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier.testTag("backButton")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.cancel)
                )
            }
        },
        modifier = modifier.testTag("medicationDetailsTopBar")
    )
}

@Composable
private fun DetailsBody(
    state: MedicationDetailsState,
    onEvent: (MedicationDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.drug != null -> {
                DetailsContent(
                    drug = state.drug,
                    currentTimeMillis = state.currentTimeMillis,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    drug: Drug,
    currentTimeMillis: Long,
    onEvent: (MedicationDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isReady = TimeUtils.isDoseReady(drug.dragTime, drug.timeInterval, currentTimeMillis)
    val remainingMillis = TimeUtils.getRemainingMillis(drug.dragTime, drug.timeInterval, currentTimeMillis)
    val progress = TimeUtils.calculateProgress(drug.dragTime, drug.timeInterval, currentTimeMillis)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusCard(
            isReady = isReady,
            progress = progress,
            remainingMillis = remainingMillis
        )

        InfoCard(
            drug = drug
        )

        MainActionsSection(
            isReady = isReady,
            onTakeDrugClicked = { onEvent(MedicationDetailsEvent.OnTakeDrugClicked) },
            onResetTimerClicked = { onEvent(MedicationDetailsEvent.OnResetTimerClicked) },
            onEditDrugClicked = { onEvent(MedicationDetailsEvent.OnEditDrugClicked) },
            onDeleteDrugClicked = { onEvent(MedicationDetailsEvent.OnDeleteDrugClicked) }
        )
    }
}

@Composable
private fun StatusCard(
    isReady: Boolean,
    progress: Float,
    remainingMillis: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isReady) {
                    stringResource(id = R.string.time_for_drug)
                } else {
                    stringResource(id = R.string.remaining_time)
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isReady) {
                    stringResource(id = R.string.ready_to_take)
                } else {
                    TimeUtils.formatRemainingTime(remainingMillis)
                },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = if (isReady) FontFamily.Default else FontFamily.Monospace
                ),
                color = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun InfoCard(
    drug: Drug,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            InfoRow(
                label = stringResource(id = R.string.drug_name),
                value = drug.name
            )
            InfoRow(
                label = stringResource(id = R.string.interval_size),
                value = TimeUtils.formatRemainingTime(drug.timeInterval)
            )
            InfoRow(
                label = stringResource(id = R.string.last_dose_time),
                value = TimeUtils.formatTimestamp(drug.dragTime)
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun MainActionsSection(
    isReady: Boolean,
    onTakeDrugClicked: () -> Unit,
    onResetTimerClicked: () -> Unit,
    onEditDrugClicked: () -> Unit,
    onDeleteDrugClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onTakeDrugClicked,
            enabled = isReady,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("detailsTakeDrugButton")
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = stringResource(id = R.string.take_drug),
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick = onResetTimerClicked,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("resetTimerButton")
        ) {
            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = stringResource(id = R.string.reset_timer))
        }

        OutlinedButton(
            onClick = onEditDrugClicked,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("editMedicationButton")
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = stringResource(id = R.string.edit_drug))
        }

        OutlinedButton(
            onClick = onDeleteDrugClicked,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("deleteMedicationButton")
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = stringResource(id = R.string.delete_drug))
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.delete_confirmation_title)) },
        text = { Text(text = stringResource(id = R.string.delete_confirmation_message)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.testTag("confirmDeleteButton")
            ) {
                Text(text = stringResource(id = R.string.confirm_delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("cancelDeleteButton")
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    )
}

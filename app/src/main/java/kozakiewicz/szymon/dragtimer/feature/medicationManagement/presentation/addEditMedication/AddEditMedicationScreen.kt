package kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.R

/**
 * Screen enabling users to create a new medication or modify an existing medication's settings.
 *
 * Purpose: Provides a form for configuring medication title and interval duration (hours, minutes, seconds).
 * Available Functionalities: Enter drug name, configure interval components, validate input, save medication.
 * Key UI elements: Name TextField, Interval duration fields (hours, minutes, seconds), Save and Cancel buttons.
 * Navigation:
 * - Navigate From: IntervalTrackerScreen, MedicationDetailsScreen.
 * - Navigate To: Back to source screen upon saving or canceling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedicationScreen(
    state: AddEditMedicationState,
    onEventFromViewModel: (AddEditMedicationEvent) -> Unit,
    effectFromViewModel: Flow<AddEditMedicationEffect>,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFromViewModel.collect { effect ->
            when (effect) {
                is AddEditMedicationEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AddEditTopBar(
                isEditing = state.isEditing,
                onBackClicked = { onEventFromViewModel(AddEditMedicationEvent.OnBackClicked) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        AddEditMedicationForm(
            state = state,
            onEvent = onEventFromViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditTopBar(
    isEditing: Boolean,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = if (isEditing) {
                    stringResource(id = R.string.edit_drug)
                } else {
                    stringResource(id = R.string.add_drug)
                }
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
        modifier = modifier.testTag("addEditTopBar")
    )
}

@Composable
private fun AddEditMedicationForm(
    state: AddEditMedicationState,
    onEvent: (AddEditMedicationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DrugNameSection(
            name = state.name,
            error = state.nameError,
            onNameChanged = { onEvent(AddEditMedicationEvent.OnNameChanged(it)) }
        )

        IntervalSection(
            hours = state.hours,
            minutes = state.minutes,
            seconds = state.seconds,
            error = state.intervalError,
            onHoursChanged = { onEvent(AddEditMedicationEvent.OnHoursChanged(it)) },
            onMinutesChanged = { onEvent(AddEditMedicationEvent.OnMinutesChanged(it)) },
            onSecondsChanged = { onEvent(AddEditMedicationEvent.OnSecondsChanged(it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionButtonsSection(
            isEditing = state.isEditing,
            onSaveClicked = { onEvent(AddEditMedicationEvent.OnSaveClicked) },
            onCancelClicked = { onEvent(AddEditMedicationEvent.OnBackClicked) }
        )
    }
}

@Composable
private fun DrugNameSection(
    name: String,
    error: String?,
    onNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text(text = stringResource(id = R.string.drug_name)) },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            isError = error != null,
            supportingText = {
                if (error != null) {
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("drugNameTextField")
        )
    }
}

@Composable
private fun IntervalSection(
    hours: String,
    minutes: String,
    seconds: String,
    error: String?,
    onHoursChanged: (String) -> Unit,
    onMinutesChanged: (String) -> Unit,
    onSecondsChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.interval_size),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IntervalInputField(
                value = hours,
                onValueChange = onHoursChanged,
                label = stringResource(id = R.string.hours),
                testTag = "hoursTextField",
                modifier = Modifier.weight(1f)
            )
            IntervalInputField(
                value = minutes,
                onValueChange = onMinutesChanged,
                label = stringResource(id = R.string.minutes),
                testTag = "minutesTextField",
                modifier = Modifier.weight(1f)
            )
            IntervalInputField(
                value = seconds,
                onValueChange = onSecondsChanged,
                label = stringResource(id = R.string.seconds),
                testTag = "secondsTextField",
                modifier = Modifier.weight(1f)
            )
        }
        if (error != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun IntervalInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    testTag: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier.testTag(testTag)
    )
}

@Composable
private fun ActionButtonsSection(
    isEditing: Boolean,
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onCancelClicked,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("cancelButton")
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        Button(
            onClick = onSaveClicked,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("saveMedicationButton")
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(
                text = if (isEditing) {
                    stringResource(id = R.string.save)
                } else {
                    stringResource(id = R.string.add_drug)
                }
            )
        }
    }
}

package kozakiewicz.szymon.dragtimer.featuresTests.medicationManagementTests

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.navigation.MedicationManagementScreen
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationEvent
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationNavEffect
import kozakiewicz.szymon.dragtimer.feature.medicationManagement.presentation.addEditMedication.AddEditMedicationViewModel
import kozakiewicz.szymon.dragtimer.feature.notifications.scheduler.NotificationScheduler
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditMedicationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val drugRepository: DrugRepository = mockk(relaxed = true)
    private val notificationScheduler: NotificationScheduler = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onEvent_whenNameIsEmpty_setsNameError() = runTest(testDispatcher) {
        val savedStateHandle: SavedStateHandle = mockk()
        every { savedStateHandle.toRoute<MedicationManagementScreen.AddEdit>() } returns MedicationManagementScreen.AddEdit()

        val viewModel = AddEditMedicationViewModel(drugRepository, notificationScheduler, savedStateHandle)
        viewModel.onEvent(AddEditMedicationEvent.OnNameChanged(""))
        viewModel.onEvent(AddEditMedicationEvent.OnHoursChanged("2"))
        viewModel.onEvent(AddEditMedicationEvent.OnSaveClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.nameError)
    }

    @Test
    fun onEvent_whenIntervalIsZero_setsIntervalError() = runTest(testDispatcher) {
        val savedStateHandle: SavedStateHandle = mockk()
        every { savedStateHandle.toRoute<MedicationManagementScreen.AddEdit>() } returns MedicationManagementScreen.AddEdit()

        val viewModel = AddEditMedicationViewModel(drugRepository, notificationScheduler, savedStateHandle)
        viewModel.onEvent(AddEditMedicationEvent.OnNameChanged("Ibuprofen"))
        viewModel.onEvent(AddEditMedicationEvent.OnHoursChanged("0"))
        viewModel.onEvent(AddEditMedicationEvent.OnMinutesChanged("0"))
        viewModel.onEvent(AddEditMedicationEvent.OnSecondsChanged("0"))
        viewModel.onEvent(AddEditMedicationEvent.OnSaveClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.intervalError)
    }

    @Test
    fun onEvent_whenInputIsValid_insertsDrugAndNavigatesBack() = runTest(testDispatcher) {
        val savedStateHandle: SavedStateHandle = mockk()
        every { savedStateHandle.toRoute<MedicationManagementScreen.AddEdit>() } returns MedicationManagementScreen.AddEdit()
        coEvery { drugRepository.insertDrug(any()) } returns 1L

        val viewModel = AddEditMedicationViewModel(drugRepository, notificationScheduler, savedStateHandle)
        viewModel.onEvent(AddEditMedicationEvent.OnNameChanged("Ibuprofen"))
        viewModel.onEvent(AddEditMedicationEvent.OnHoursChanged("4"))

        viewModel.navEffect.test {
            viewModel.onEvent(AddEditMedicationEvent.OnSaveClicked)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertEquals(AddEditMedicationNavEffect.NavigateBack, effect)
            coVerify { drugRepository.insertDrug(match { it.name == "Ibuprofen" && it.timeInterval == 14400000L }) }
        }
    }
}

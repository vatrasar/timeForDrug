package kozakiewicz.szymon.dragtimer.featuresTests.intervalTrackerTests

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerEvent
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerNavEffect
import kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.IntervalTrackerViewModel
import kozakiewicz.szymon.dragtimer.feature.notifications.scheduler.NotificationScheduler
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IntervalTrackerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val drugRepository: DrugRepository = mockk(relaxed = true)
    private val notificationScheduler: NotificationScheduler = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun observeDrugs_whenRepositoryEmitsList_updatesStateWithDrugs() = runTest(testDispatcher) {
        val sampleDrugs = listOf(
            Drug(id = 1, name = "Apap", timeInterval = 3600000L, dragTime = 0L)
        )
        every { drugRepository.getAllDrugs() } returns flowOf(sampleDrugs)

        val viewModel = IntervalTrackerViewModel(drugRepository, notificationScheduler).apply { stopLiveTimer() }
        testDispatcher.scheduler.runCurrent()

        assertEquals(sampleDrugs, viewModel.state.value.drugs)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun onEvent_whenAddDrugClicked_emitsNavigateToAddDrugEffect() = runTest(testDispatcher) {
        every { drugRepository.getAllDrugs() } returns flowOf(emptyList())
        val viewModel = IntervalTrackerViewModel(drugRepository, notificationScheduler).apply { stopLiveTimer() }

        viewModel.navEffect.test {
            viewModel.onEvent(IntervalTrackerEvent.OnAddDrugClicked)
            testDispatcher.scheduler.runCurrent()

            val effect = awaitItem()
            assertEquals(IntervalTrackerNavEffect.NavigateToAddDrug, effect)
        }
    }

    @Test
    fun onEvent_whenDrugCardClicked_emitsNavigateToDrugDetailsEffect() = runTest(testDispatcher) {
        every { drugRepository.getAllDrugs() } returns flowOf(emptyList())
        val viewModel = IntervalTrackerViewModel(drugRepository, notificationScheduler).apply { stopLiveTimer() }

        viewModel.navEffect.test {
            viewModel.onEvent(IntervalTrackerEvent.OnDrugCardClicked(drugId = 42))
            testDispatcher.scheduler.runCurrent()

            val effect = awaitItem()
            assertEquals(IntervalTrackerNavEffect.NavigateToDrugDetails(42), effect)
        }
    }

    @Test
    fun onEvent_whenTakeDrugClicked_updatesDrugAndRecordsEvent() = runTest(testDispatcher) {
        val drug = Drug(id = 1, name = "Apap", timeInterval = 3600000L, dragTime = 0L)
        every { drugRepository.getAllDrugs() } returns flowOf(listOf(drug))
        coEvery { drugRepository.updateDrug(any()) } returns Unit
        coEvery { drugRepository.recordDrugTaken(any(), any()) } returns Unit

        val viewModel = IntervalTrackerViewModel(drugRepository, notificationScheduler).apply { stopLiveTimer() }
        testDispatcher.scheduler.runCurrent()

        viewModel.onEvent(IntervalTrackerEvent.OnTakeDrugClicked(drugId = 1))
        testDispatcher.scheduler.runCurrent()

        coVerify { drugRepository.updateDrug(match { it.id == 1 && it.dragTime > 0L }) }
        coVerify { drugRepository.recordDrugTaken(1, any()) }
        coVerify { notificationScheduler.scheduleDoseAlarm(1, "Apap", any()) }
    }
}

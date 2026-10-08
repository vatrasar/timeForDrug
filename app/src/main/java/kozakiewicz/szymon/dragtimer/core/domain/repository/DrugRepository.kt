package kozakiewicz.szymon.dragtimer.core.domain.repository

import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent

interface DrugRepository {

    /**
     * Observes all medications stored in the database.
     * Invoked by: IntervalTrackerViewModel, StatisticsViewModel.
     */
    fun getAllDrugs(): Flow<List<Drug>>

    /**
     * Retrieves all medications once directly without observing.
     * Invoked by: MedicationAlarmReceiver.
     */
    suspend fun getAllDrugsDirect(): List<Drug>

    /**
     * Observes a specific medication by its unique identifier.
     * Invoked by: MedicationDetailsViewModel.
     */
    fun getDrugById(id: Int): Flow<Drug?>

    /**
     * Retrieves a specific medication once directly.
     * Invoked by: AddEditMedicationViewModel.
     */
    suspend fun getDrugByIdDirect(id: Int): Drug?

    /**
     * Inserts a new medication or updates an existing one.
     * Invoked by: AddEditMedicationViewModel.
     */
    suspend fun insertDrug(drug: Drug): Long

    /**
     * Updates an existing medication.
     * Invoked by: IntervalTrackerViewModel, MedicationDetailsViewModel, AddEditMedicationViewModel.
     */
    suspend fun updateDrug(drug: Drug)

    /**
     * Deletes a medication and its associated dose history.
     * Invoked by: MedicationDetailsViewModel.
     */
    suspend fun deleteDrug(drug: Drug)

    /**
     * Records a medication dose event in the history log.
     * Invoked by: IntervalTrackerViewModel, MedicationDetailsViewModel.
     */
    suspend fun recordDrugTaken(drugId: Int, timestamp: Long)

    /**
     * Observes all dose intake events in the history log.
     * Invoked by: DoseHistoryViewModel.
     */
    fun getAllDoseEvents(): Flow<List<TakeDrugEvent>>

    /**
     * Observes dose intake events for a specific medication.
     * Invoked by: DoseHistoryViewModel, StatisticsViewModel.
     */
    fun getDoseEventsForDrug(drugId: Int): Flow<List<TakeDrugEvent>>

    /**
     * Observes dose events within a specific time range.
     * Invoked by: StatisticsViewModel.
     */
    fun getDoseEventsBetween(startTime: Long, endTime: Long): Flow<List<TakeDrugEvent>>

    /**
     * Retrieves dose events for a medication and a day range directly.
     * Invoked by: StatisticsViewModel.
     */
    suspend fun getEventsForDrugAndDay(drugId: Int, startOfDay: Long, endOfDay: Long): List<TakeDrugEvent>
}

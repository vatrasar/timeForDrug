package kozakiewicz.szymon.dragtimer.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kozakiewicz.szymon.dragtimer.core.data.local.dao.DrugDao
import kozakiewicz.szymon.dragtimer.core.data.local.dao.TakeDrugEventDao
import kozakiewicz.szymon.dragtimer.core.data.local.entities.TakeDrugEventEntity
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toDomain
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toEntity
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import javax.inject.Inject

class DrugRepositoryImpl @Inject constructor(
    private val drugDao: DrugDao,
    private val takeDrugEventDao: TakeDrugEventDao
) : DrugRepository {

    /**
     * Observes all medications stored in the database.
     * Invoked by: IntervalTrackerViewModel, StatisticsViewModel.
     */
    override fun getAllDrugs(): Flow<List<Drug>> {
        return drugDao.getAllDrugsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Retrieves all medications once directly without observing.
     * Invoked by: MedicationAlarmReceiver.
     */
    override suspend fun getAllDrugsDirect(): List<Drug> {
        return drugDao.getAllDrugsDirect().map { it.toDomain() }
    }

    /**
     * Observes a specific medication by its unique identifier.
     * Invoked by: MedicationDetailsViewModel.
     */
    override fun getDrugById(id: Int): Flow<Drug?> {
        return drugDao.getDrugByIdFlow(id).map { it?.toDomain() }
    }

    /**
     * Retrieves a specific medication once directly.
     * Invoked by: AddEditMedicationViewModel.
     */
    override suspend fun getDrugByIdDirect(id: Int): Drug? {
        return drugDao.getDrugByIdDirect(id)?.toDomain()
    }

    /**
     * Inserts a new medication or updates an existing one.
     * Invoked by: AddEditMedicationViewModel.
     */
    override suspend fun insertDrug(drug: Drug): Long {
        return drugDao.insertDrug(drug.toEntity())
    }

    /**
     * Updates an existing medication.
     * Invoked by: IntervalTrackerViewModel, MedicationDetailsViewModel, AddEditMedicationViewModel.
     */
    override suspend fun updateDrug(drug: Drug) {
        drugDao.updateDrug(drug.toEntity())
    }

    /**
     * Deletes a medication and its associated dose history.
     * Invoked by: MedicationDetailsViewModel.
     */
    override suspend fun deleteDrug(drug: Drug) {
        drugDao.deleteDrug(drug.toEntity())
    }

    /**
     * Records a medication dose event in the history log.
     * Invoked by: IntervalTrackerViewModel, MedicationDetailsViewModel.
     */
    override suspend fun recordDrugTaken(drugId: Int, timestamp: Long) {
        val eventEntity = TakeDrugEventEntity(
            drugId = drugId,
            drugTime = timestamp
        )
        takeDrugEventDao.insertTakeDrugEvent(eventEntity)
    }

    /**
     * Observes all dose intake events in the history log.
     * Invoked by: DoseHistoryViewModel.
     */
    override fun getAllDoseEvents(): Flow<List<TakeDrugEvent>> {
        return takeDrugEventDao.getAllEventsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Observes dose intake events for a specific medication.
     * Invoked by: DoseHistoryViewModel, StatisticsViewModel.
     */
    override fun getDoseEventsForDrug(drugId: Int): Flow<List<TakeDrugEvent>> {
        return takeDrugEventDao.getEventsForDrugFlow(drugId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Observes dose events within a specific time range.
     * Invoked by: StatisticsViewModel.
     */
    override fun getDoseEventsBetween(startTime: Long, endTime: Long): Flow<List<TakeDrugEvent>> {
        return takeDrugEventDao.getEventsBetweenFlow(startTime, endTime).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Retrieves dose events for a medication and a day range directly.
     * Invoked by: StatisticsViewModel.
     */
    override suspend fun getEventsForDrugAndDay(
        drugId: Int,
        startOfDay: Long,
        endOfDay: Long
    ): List<TakeDrugEvent> {
        return takeDrugEventDao.getEventsForDrugAndDay(drugId, startOfDay, endOfDay).map {
            it.toDomain()
        }
    }
}

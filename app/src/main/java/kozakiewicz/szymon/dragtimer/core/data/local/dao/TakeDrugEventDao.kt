package kozakiewicz.szymon.dragtimer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.core.data.local.entities.TakeDrugEventEntity

@Dao
interface TakeDrugEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTakeDrugEvent(event: TakeDrugEventEntity): Long

    @Query("SELECT * FROM Statistic ORDER BY drugTime DESC")
    fun getAllEventsFlow(): Flow<List<TakeDrugEventEntity>>

    @Query("SELECT * FROM Statistic WHERE drugId = :drugId ORDER BY drugTime DESC")
    fun getEventsForDrugFlow(drugId: Int): Flow<List<TakeDrugEventEntity>>

    @Query("SELECT * FROM Statistic WHERE drugId = :drugId AND drugTime BETWEEN :startOfDay AND :endOfDay ORDER BY drugTime DESC")
    suspend fun getEventsForDrugAndDay(drugId: Int, startOfDay: Long, endOfDay: Long): List<TakeDrugEventEntity>

    @Query("SELECT * FROM Statistic WHERE drugTime BETWEEN :startTime AND :endTime ORDER BY drugTime DESC")
    fun getEventsBetweenFlow(startTime: Long, endTime: Long): Flow<List<TakeDrugEventEntity>>
}

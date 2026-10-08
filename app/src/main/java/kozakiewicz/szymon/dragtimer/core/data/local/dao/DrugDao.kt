package kozakiewicz.szymon.dragtimer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kozakiewicz.szymon.dragtimer.core.data.local.entities.DrugEntity

@Dao
interface DrugDao {

    @Query("SELECT * FROM Drag ORDER BY name ASC")
    fun getAllDrugsFlow(): Flow<List<DrugEntity>>

    @Query("SELECT * FROM Drag ORDER BY name ASC")
    suspend fun getAllDrugsDirect(): List<DrugEntity>

    @Query("SELECT * FROM Drag WHERE id = :id LIMIT 1")
    fun getDrugByIdFlow(id: Int): Flow<DrugEntity?>

    @Query("SELECT * FROM Drag WHERE id = :id LIMIT 1")
    suspend fun getDrugByIdDirect(id: Int): DrugEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrug(drug: DrugEntity): Long

    @Update
    suspend fun updateDrug(drug: DrugEntity)

    @Delete
    suspend fun deleteDrug(drug: DrugEntity)

    @Query("DELETE FROM Drag WHERE id = :id")
    suspend fun deleteDrugById(id: Int)
}

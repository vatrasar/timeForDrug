package kozakiewicz.szymon.dragtimer.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DragDao {

    @Insert
    fun insert(newDrug:Drug)

    @Delete
    fun delete(drugToDelete:Drug)

    @Update
    fun update(drugToUpdate:Drug)

    @Query("SELECT * FROM Drag")
    fun getAllDrags():LiveData<List<Drug>>



}
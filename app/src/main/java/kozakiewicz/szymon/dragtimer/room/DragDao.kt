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

    @Insert
    fun insertTakeDrugStac(drugWithStac:TakeDrugEvent)

    @Query("SELECT * FROM Drag")
    fun getAllDrags():LiveData<List<Drug>>

    @Query("SELECT * FROM Drag")
    fun getAllDragsList():List<Drug>

    @Query("SELECT*FROM Statistic WHERE drugId=(:drugId)")
    fun getStacForDrug(drugId:Int):List<TakeDrugEvent>
    @Query("SELECT*FROM Statistic WHERE drugId=(:drugId) AND drugTime BETWEEN (:startOfDay) AND (:endOfDay)")
    fun getStacForDrugAndDay(drugId:Int, startOfDay: Long, endOfDay: Long): List<TakeDrugEvent>

}
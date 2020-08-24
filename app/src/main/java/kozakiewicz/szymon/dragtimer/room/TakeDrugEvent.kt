package kozakiewicz.szymon.dragtimer.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Statistic",foreignKeys = arrayOf(ForeignKey(entity = Drug::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("drugId"),
        onDelete = CASCADE)))
data class TakeDrugEvent(public var drugTime:Long,var drugId:Int)
{
    @PrimaryKey(autoGenerate = true)
    var idDrugEvent:Int=0
}
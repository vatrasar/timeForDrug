package kozakiewicz.szymon.dragtimer.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Drag")
data class Drug(var name:String, var timeInterval:Long, var dragTime:Long){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
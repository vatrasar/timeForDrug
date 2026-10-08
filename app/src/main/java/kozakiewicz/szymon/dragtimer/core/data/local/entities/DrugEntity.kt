package kozakiewicz.szymon.dragtimer.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Drag")
data class DrugEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val timeInterval: Long,
    val dragTime: Long
)

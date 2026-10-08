package kozakiewicz.szymon.dragtimer.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Statistic",
    foreignKeys = [
        ForeignKey(
            entity = DrugEntity::class,
            parentColumns = ["id"],
            childColumns = ["drugId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["drugId"])
    ]
)
data class TakeDrugEventEntity(
    @PrimaryKey(autoGenerate = true)
    val idDrugEvent: Int = 0,
    val drugTime: Long,
    val drugId: Int
)

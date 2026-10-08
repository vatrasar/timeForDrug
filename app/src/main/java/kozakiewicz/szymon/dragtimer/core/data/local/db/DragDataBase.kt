package kozakiewicz.szymon.dragtimer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kozakiewicz.szymon.dragtimer.core.data.local.dao.DrugDao
import kozakiewicz.szymon.dragtimer.core.data.local.dao.TakeDrugEventDao
import kozakiewicz.szymon.dragtimer.core.data.local.entities.DrugEntity
import kozakiewicz.szymon.dragtimer.core.data.local.entities.TakeDrugEventEntity

@Database(
    entities = [DrugEntity::class, TakeDrugEventEntity::class],
    version = 2,
    exportSchema = false
)
abstract class DragDataBase : RoomDatabase() {

    abstract fun drugDao(): DrugDao

    abstract fun takeDrugEventDao(): TakeDrugEventDao

    companion object {
        const val DATABASE_NAME = "dragDatabase"
    }
}

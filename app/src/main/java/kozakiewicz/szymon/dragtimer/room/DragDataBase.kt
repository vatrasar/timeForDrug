package kozakiewicz.szymon.dragtimer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Drug::class,TakeDrugEvent::class],version = 2,exportSchema = false)
abstract class DragDataBase:RoomDatabase()
{
    abstract fun DragDao():DragDao
    companion object{
        var instance:DragDataBase?=null
        fun getInstance(context:Context):DragDataBase?
        {
            if(instance==null) {
                instance = Room.databaseBuilder(context, DragDataBase::class.java, "dragDatabase").fallbackToDestructiveMigration().build()
            }
            return instance
        }

    }

}
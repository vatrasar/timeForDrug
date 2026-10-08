package kozakiewicz.szymon.dragtimer.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kozakiewicz.szymon.dragtimer.core.data.local.dao.DrugDao
import kozakiewicz.szymon.dragtimer.core.data.local.dao.TakeDrugEventDao
import kozakiewicz.szymon.dragtimer.core.data.local.db.DragDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDragDatabase(
        @ApplicationContext context: Context
    ): DragDataBase {
        return Room.databaseBuilder(
            context,
            DragDataBase::class.java,
            DragDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideDrugDao(database: DragDataBase): DrugDao {
        return database.drugDao()
    }

    @Provides
    fun provideTakeDrugEventDao(database: DragDataBase): TakeDrugEventDao {
        return database.takeDrugEventDao()
    }
}

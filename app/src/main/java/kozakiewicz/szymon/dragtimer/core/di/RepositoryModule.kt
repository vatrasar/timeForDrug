package kozakiewicz.szymon.dragtimer.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kozakiewicz.szymon.dragtimer.core.data.repository.DrugRepositoryImpl
import kozakiewicz.szymon.dragtimer.core.domain.repository.DrugRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDrugRepository(
        impl: DrugRepositoryImpl
    ): DrugRepository
}

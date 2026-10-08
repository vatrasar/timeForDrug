package kozakiewicz.szymon.dragtimer.coreTests.mapperTests

import kozakiewicz.szymon.dragtimer.core.data.local.entities.TakeDrugEventEntity
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toDomain
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toEntity
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent
import org.junit.Assert.assertEquals
import org.junit.Test

class TakeDrugEventMapperTest {

    @Test
    fun toDomain_withEntity_mapsAllFieldsCorrectly() {
        val entity = TakeDrugEventEntity(
            idDrugEvent = 101,
            drugTime = 1700000000000L,
            drugId = 7
        )

        val domain = entity.toDomain()

        assertEquals(entity.idDrugEvent, domain.idDrugEvent)
        assertEquals(entity.drugTime, domain.drugTime)
        assertEquals(entity.drugId, domain.drugId)
    }

    @Test
    fun toEntity_withDomainModel_mapsAllFieldsCorrectly() {
        val domain = TakeDrugEvent(
            idDrugEvent = 202,
            drugTime = 1700000500000L,
            drugId = 9
        )

        val entity = domain.toEntity()

        assertEquals(domain.idDrugEvent, entity.idDrugEvent)
        assertEquals(domain.drugTime, entity.drugTime)
        assertEquals(domain.drugId, entity.drugId)
    }
}

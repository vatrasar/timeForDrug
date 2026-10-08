package kozakiewicz.szymon.dragtimer.coreTests.mapperTests

import kozakiewicz.szymon.dragtimer.core.data.local.entities.DrugEntity
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toDomain
import kozakiewicz.szymon.dragtimer.core.data.local.mapper.toEntity
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug
import org.junit.Assert.assertEquals
import org.junit.Test

class DrugMapperTest {

    @Test
    fun toDomain_withEntity_mapsAllFieldsCorrectly() {
        val entity = DrugEntity(
            id = 12,
            name = "Paracetamol",
            timeInterval = 14400000L,
            dragTime = 1700000000000L
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.timeInterval, domain.timeInterval)
        assertEquals(entity.dragTime, domain.dragTime)
    }

    @Test
    fun toEntity_withDomainModel_mapsAllFieldsCorrectly() {
        val domain = Drug(
            id = 5,
            name = "Ibuprofen",
            timeInterval = 28800000L,
            dragTime = 1700000005000L
        )

        val entity = domain.toEntity()

        assertEquals(domain.id, entity.id)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.timeInterval, entity.timeInterval)
        assertEquals(domain.dragTime, entity.dragTime)
    }
}

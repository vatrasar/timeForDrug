package kozakiewicz.szymon.dragtimer.core.data.local.mapper

import kozakiewicz.szymon.dragtimer.core.data.local.entities.TakeDrugEventEntity
import kozakiewicz.szymon.dragtimer.core.domain.model.TakeDrugEvent

fun TakeDrugEventEntity.toDomain(): TakeDrugEvent {
    return TakeDrugEvent(
        idDrugEvent = idDrugEvent,
        drugTime = drugTime,
        drugId = drugId
    )
}

fun TakeDrugEvent.toEntity(): TakeDrugEventEntity {
    return TakeDrugEventEntity(
        idDrugEvent = idDrugEvent,
        drugTime = drugTime,
        drugId = drugId
    )
}

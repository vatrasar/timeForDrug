package kozakiewicz.szymon.dragtimer.core.data.local.mapper

import kozakiewicz.szymon.dragtimer.core.data.local.entities.DrugEntity
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

fun DrugEntity.toDomain(): Drug {
    return Drug(
        id = id,
        name = name,
        timeInterval = timeInterval,
        dragTime = dragTime
    )
}

fun Drug.toEntity(): DrugEntity {
    return DrugEntity(
        id = id,
        name = name,
        timeInterval = timeInterval,
        dragTime = dragTime
    )
}

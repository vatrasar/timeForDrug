package kozakiewicz.szymon.dragtimer.core.domain.model

data class TakeDrugEvent(
    val idDrugEvent: Int = 0,
    val drugTime: Long,
    val drugId: Int
)

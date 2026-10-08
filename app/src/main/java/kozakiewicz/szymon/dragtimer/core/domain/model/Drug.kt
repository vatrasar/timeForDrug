package kozakiewicz.szymon.dragtimer.core.domain.model

data class Drug(
    val id: Int = 0,
    val name: String,
    val timeInterval: Long,
    val dragTime: Long
)

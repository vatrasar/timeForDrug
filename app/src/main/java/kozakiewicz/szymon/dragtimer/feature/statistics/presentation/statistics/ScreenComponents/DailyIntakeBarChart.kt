package kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics.ScreenComponents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.feature.statistics.presentation.statistics.DayDoseStat

/**
 * Bar chart displaying the number of medication doses taken per day across the past seven days.
 *
 * Usage: Renders interactive bars with labels and heights proportional to daily intake numbers.
 * Key UI elements: Chart Card, Dynamic height bar columns, Day labels, Count indicator text.
 * Used In: StatisticsScreen.
 */
@Composable
fun DailyIntakeBarChart(
    stats: List<DayDoseStat>,
    modifier: Modifier = Modifier
) {
    val maxCount = stats.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dailyIntakeBarChart"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.doses_last_7_days),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            BarsRow(
                stats = stats,
                maxCount = maxCount
            )
        }
    }
}

@Composable
private fun BarsRow(
    stats: List<DayDoseStat>,
    maxCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        stats.forEach { stat ->
            BarColumn(stat = stat, maxCount = maxCount)
        }
    }
}

@Composable
private fun BarColumn(
    stat: DayDoseStat,
    maxCount: Int,
    modifier: Modifier = Modifier
) {
    val targetFraction = (stat.count.toFloat() / maxCount.toFloat()).coerceIn(0.05f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 600),
        label = "BarHeightAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier.fillMaxHeight()
    ) {
        Text(
            text = stat.count.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height((120 * animatedFraction).dp)
                .width(24.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(
                    if (stat.count > 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stat.dayLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

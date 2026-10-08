package kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.ScreenComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kozakiewicz.szymon.dragtimer.R

/**
 * Top app bar component for the interval tracker dashboard.
 *
 * Usage: Renders the primary application title centered in the app bar with active medication count.
 * Key UI elements: Application title, medication icon, active drugs count subtitle.
 * Used In: IntervalTrackerScreen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalTrackerTopBar(
    activeDrugsCount: Int,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }

                val subtitle = if (activeDrugsCount > 0) {
                    stringResource(id = R.string.active_drugs_count, activeDrugsCount)
                } else {
                    stringResource(id = R.string.active_drugs_empty)
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.testTag("intervalTrackerTopBar")
    )
}

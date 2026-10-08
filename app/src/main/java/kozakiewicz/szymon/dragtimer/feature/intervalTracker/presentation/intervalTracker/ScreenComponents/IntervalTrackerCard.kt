package kozakiewicz.szymon.dragtimer.feature.intervalTracker.presentation.intervalTracker.ScreenComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.common.util.TimeUtils
import kozakiewicz.szymon.dragtimer.core.domain.model.Drug

/**
 * Card component presenting an individual medication with its live interval countdown and action buttons.
 *
 * Usage: Displays medication name, status badge, interval progress, and actions to take dose or view details.
 * Key UI elements: Drug icon and name, status pill, countdown panel, LinearProgressIndicator, Take dose button.
 * Used In: IntervalTrackerScreen.
 */
@Composable
fun IntervalTrackerCard(
    drug: Drug,
    currentTimeMillis: Long,
    onTakeDrugClicked: (Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = TimeUtils.isDoseReady(drug.dragTime, drug.timeInterval, currentTimeMillis)
    val remainingMillis = TimeUtils.getRemainingMillis(drug.dragTime, drug.timeInterval, currentTimeMillis)
    val progress = TimeUtils.calculateProgress(drug.dragTime, drug.timeInterval, currentTimeMillis)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailsClicked(drug.id) }
            .testTag("drugCard_${drug.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            HeaderSection(
                drug = drug,
                isReady = isReady,
                onDetailsClicked = { onDetailsClicked(drug.id) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            CountdownSection(
                isReady = isReady,
                progress = progress,
                remainingMillis = remainingMillis
            )

            Spacer(modifier = Modifier.height(14.dp))

            TakeDoseButton(
                drugId = drug.id,
                isReady = isReady,
                onTakeDrugClicked = onTakeDrugClicked
            )
        }
    }
}

@Composable
private fun HeaderSection(
    drug: Drug,
    isReady: Boolean,
    onDetailsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MedicationIcon(isReady = isReady)

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = drug.name,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDetailsClicked,
            modifier = Modifier
                .size(36.dp)
                .testTag("detailsButton_${drug.id}")
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.details),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MedicationIcon(
    isReady: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isReady) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val iconColor = if (isReady) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Medication,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun StatusPill(
    isReady: Boolean,
    modifier: Modifier = Modifier
) {
    val pillBg = if (isReady) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val pillBorderColor = if (isReady) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val pillTextColor = if (isReady) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(pillBg)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(pillTextColor)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isReady) {
                    stringResource(id = R.string.status_ready)
                } else {
                    stringResource(id = R.string.status_running)
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = pillTextColor
            )
        }
    }
}

@Composable
private fun CountdownSection(
    isReady: Boolean,
    progress: Float,
    remainingMillis: Long,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.remaining_time).uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                StatusPill(isReady = isReady)
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (isReady) {
                Text(
                    text = stringResource(id = R.string.time_for_drug),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Text(
                    text = TimeUtils.formatRemainingTime(remainingMillis),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isReady) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun TakeDoseButton(
    drugId: Int,
    isReady: Boolean,
    onTakeDrugClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onTakeDrugClicked(drugId) },
        enabled = isReady,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("takeDrugButton_$drugId")
    ) {
        Icon(
            imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.HourglassEmpty,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isReady) {
                stringResource(id = R.string.take_drug)
            } else {
                stringResource(id = R.string.wait_for_next_dose)
            },
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

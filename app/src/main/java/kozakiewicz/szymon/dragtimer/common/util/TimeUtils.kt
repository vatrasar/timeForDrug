package kozakiewicz.szymon.dragtimer.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    /**
     * Checks if the time interval has elapsed and a new dose should be taken.
     * Invoked by: IntervalTrackerViewModel, IntervalTrackerCard, MedicationDetailsViewModel.
     */
    fun isDoseReady(
        lastDrugTime: Long,
        intervalMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {
        if (intervalMillis <= 0L) return true
        return (currentTimeMillis - lastDrugTime) >= intervalMillis
    }

    /**
     * Computes the remaining milliseconds until the next scheduled dose.
     * Invoked by: IntervalTrackerViewModel, IntervalTrackerCard, MedicationDetailsViewModel.
     */
    fun getRemainingMillis(
        lastDrugTime: Long,
        intervalMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Long {
        val elapsed = currentTimeMillis - lastDrugTime
        val remaining = intervalMillis - elapsed
        return if (remaining > 0L) remaining else 0L
    }

    /**
     * Computes the fraction of interval completed from 0.0 to 1.0.
     * Invoked by: IntervalTrackerCard, MedicationDetailsScreen.
     */
    fun calculateProgress(
        lastDrugTime: Long,
        intervalMillis: Long,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Float {
        if (intervalMillis <= 0L) return 1f
        val elapsed = currentTimeMillis - lastDrugTime
        if (elapsed <= 0L) return 0f
        if (elapsed >= intervalMillis) return 1f
        return elapsed.toFloat() / intervalMillis.toFloat()
    }

    /**
     * Formats milliseconds into a standard HH:mm:ss countdown display.
     * Invoked by: IntervalTrackerCard, MedicationDetailsScreen.
     */
    fun formatRemainingTime(remainingMillis: Long): String {
        val totalSeconds = remainingMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    /**
     * Formats an epoch timestamp into a human readable date and time string.
     * Invoked by: DoseHistoryScreen, MedicationDetailsScreen.
     */
    fun formatTimestamp(timestampMillis: Long): String {
        if (timestampMillis <= 0L) return "-"
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return formatter.format(Date(timestampMillis))
    }

    /**
     * Converts hours, minutes, and seconds into total milliseconds.
     * Invoked by: AddEditMedicationViewModel.
     */
    fun toMillis(hours: Int, minutes: Int, seconds: Int): Long {
        val totalSecs = (hours.toLong() * 3600L) + (minutes.toLong() * 60L) + seconds.toLong()
        return totalSecs * 1000L
    }

    /**
     * Extracts full hours from interval milliseconds.
     * Invoked by: AddEditMedicationViewModel.
     */
    fun millisToHours(millis: Long): Int {
        return (millis / (1000L * 3600L)).toInt()
    }

    /**
     * Extracts minutes component from interval milliseconds.
     * Invoked by: AddEditMedicationViewModel.
     */
    fun millisToMinutes(millis: Long): Int {
        val seconds = (millis / 1000L) % 3600L
        return (seconds / 60L).toInt()
    }

    /**
     * Extracts seconds component from interval milliseconds.
     * Invoked by: AddEditMedicationViewModel.
     */
    fun millisToSeconds(millis: Long): Int {
        return ((millis / 1000L) % 60L).toInt()
    }
}

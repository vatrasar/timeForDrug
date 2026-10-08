package kozakiewicz.szymon.dragtimer.feature.notifications.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kozakiewicz.szymon.dragtimer.feature.notifications.receiver.MedicationAlarmReceiver
import kozakiewicz.szymon.dragtimer.feature.notifications.util.NotificationConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    /**
     * Schedules an alarm to notify the user when the next dose time arrives.
     * Invoked by: IntervalTrackerViewModel, MedicationDetailsViewModel, AddEditMedicationViewModel.
     */
    fun scheduleDoseAlarm(drugId: Int, drugName: String, triggerTimeMillis: Long) {
        val pendingIntent = createAlarmPendingIntent(drugId, drugName)
        scheduleExactOrInexact(triggerTimeMillis, pendingIntent)
    }

    /**
     * Cancels any pending dose alarm for the given medication.
     * Invoked by: MedicationDetailsViewModel.
     */
    fun cancelDoseAlarm(drugId: Int, drugName: String) {
        val pendingIntent = createAlarmPendingIntent(drugId, drugName)
        alarmManager?.cancel(pendingIntent)
    }

    private fun scheduleExactOrInexact(triggerTimeMillis: Long, pendingIntent: PendingIntent) {
        if (alarmManager == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }

    private fun createAlarmPendingIntent(drugId: Int, drugName: String): PendingIntent {
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = NotificationConstants.ACTION_DOSE_ALARM
            putExtra(NotificationConstants.EXTRA_DRUG_ID, drugId)
            putExtra(NotificationConstants.EXTRA_DRUG_NAME, drugName)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(context, drugId, intent, flags)
    }
}

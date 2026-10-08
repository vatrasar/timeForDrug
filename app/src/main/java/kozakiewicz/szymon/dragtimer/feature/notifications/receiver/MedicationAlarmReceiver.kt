package kozakiewicz.szymon.dragtimer.feature.notifications.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kozakiewicz.szymon.dragtimer.MainActivity
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.feature.notifications.util.NotificationConstants

class MedicationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val drugId = intent.getIntExtra(NotificationConstants.EXTRA_DRUG_ID, 0)
        val drugName = intent.getStringExtra(NotificationConstants.EXTRA_DRUG_NAME).orEmpty()
        showNotification(context, drugId, drugName)
    }

    private fun showNotification(context: Context, drugId: Int, drugName: String) {
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, drugId, launchIntent, flags)

        val title = context.getString(R.string.notification_title)
        val content = context.getString(R.string.notification_content, drugName)

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.notify_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationId = NotificationConstants.NOTIFICATION_ID_BASE + drugId
        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(notificationId, builder.build())
        } catch (_: SecurityException) {
        }
    }
}

package kozakiewicz.szymon.dragtimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotifyManager extends BroadcastReceiver {


    public NotifyManager() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "l")
                .setSmallIcon(R.drawable.notify_icon)
                .setContentTitle("Czas na lek")
                .setContentText("Weź swój lek")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

        Intent intentService = new Intent();
        intent.setClass(context, AlarmService.class);
        context.stopService(intent);
    }

    public void setAlarm(Context context, long remaningTime)
    {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "l")
//                .setSmallIcon(R.drawable.notify_icon)
//                .setContentTitle("czas "+remaningTime)
//                .setContentText("Weź swój lek:" + remaningTime*60*1000)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(1, builder.build());
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NotifyManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, remaningTime, pi);
    }
}

package kozakiewicz.szymon.dragtimer;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmService extends Service
{


    public NotifyManager notificationManager=new NotifyManager();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



       notificationManager.setAlarm(this, getRemaningTime());

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {




        notificationManager.setAlarm(this,getRemaningTime());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    long getRemaningTime()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        long timeInterval=60000;
        timeInterval=sharedPref.getLong("timeInterval",timeInterval);

        long lastDragTime=1;
        lastDragTime=sharedPref.getLong("lastDragTime",lastDragTime);

        long nowTime=Calendar.getInstance().getTimeInMillis();
        return lastDragTime+timeInterval;
    }
}

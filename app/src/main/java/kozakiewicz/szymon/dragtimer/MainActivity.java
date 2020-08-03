package kozakiewicz.szymon.dragtimer;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int counter;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    MyTime timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        counter = 0;
        setContentView(R.layout.activity_main);
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.timeProgressbar);
        progressBar.setVisibility(View.GONE);
        if (isDragTime()) {
            setDragTimeLook();
        } else {
            //get data
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            long lastDragTime = 1;
            lastDragTime = sharedPref.getLong("lastDragTime", lastDragTime);
            int timeInterval = 1;
            timeInterval = sharedPref.getInt("hoursNumber", timeInterval);


            //set Time in timeView
            ((TextView) findViewById(R.id.txtRemainigTime)).setText(R.string.remaining_time);

            //set color:
            ((TextView) findViewById(R.id.labTime)).setTextColor(Color.BLUE);
            //run thread
            timerHandler.removeCallbacks(timerRunnable);
            timerRunnable = new MyTime((TextView) findViewById(R.id.labTime), (TextView) findViewById(R.id.txtRemainigTime), timerHandler, lastDragTime, timeInterval, (Button) findViewById(R.id.btnTakeDrag),progressBar);
            timerHandler.postDelayed(timerRunnable, 1000);

            //disable onDragTime
            Button onDragTime = ((Button) findViewById(R.id.btnTakeDrag));
            onDragTime.setEnabled(false);

            //set progrss
            setProgrss(lastDragTime, timeInterval);


        }


    }

    private void setProgrss(long lastDragTime, int timeInterval) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.timeProgressbar);;
        progressBar.setMax(timeInterval*60);
        int progress=timeInterval*60-(int) Utils.getRemaingTime(timeInterval, Calendar.getInstance().getTimeInMillis(),lastDragTime);
        progressBar.setProgress(progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setDragTimeLook() {
        TextView labTime=(TextView) findViewById(R.id.labTime);
        labTime.setText(R.string.timeForDrag);
        labTime.setTextColor(Color.GREEN);
        ((TextView) findViewById(R.id.txtRemainigTime)).setText("");
        ((Button)findViewById(R.id.btnTakeDrag)).setEnabled(true);
    }

    private boolean isDragTime() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        int timeInterval=1;
        timeInterval=sharedPref.getInt("hoursNumber",timeInterval);

        long lastDragTime=1;
        lastDragTime=sharedPref.getLong("lastDragTime",lastDragTime);
        long currentTime=Calendar.getInstance().getTimeInMillis();
        return Utils.isAfterTimeForDrag(timeInterval,currentTime,lastDragTime);
    }

    public void onSettings(View view) {
        Intent intent=new Intent(this, SettingsActivity.class);
        startActivity(intent);


    }

    public void onTakeDrag(View view) {
        ((Button)findViewById(R.id.btnTakeDrag)).setEnabled(false);

        //get Shared Preferences instance
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= sharedPref.edit();

        //get data
        long lastTime=Calendar.getInstance().getTimeInMillis();
        int timeInterval=1;
        timeInterval=sharedPref.getInt("hoursNumber",timeInterval);

        //new lastDragTime
        long lastDragTime=Calendar.getInstance().getTimeInMillis();
        editor.putLong("lastDragTime",lastDragTime);
        editor.commit();

        //set Time in timeView
        ((TextView) findViewById(R.id.txtRemainigTime)).setText(R.string.remaining_time);

        //set labRemaningtime:
        TextView labRemainingTime=((TextView) findViewById(R.id.labTime));
        labRemainingTime.setTextColor(Color.BLUE);
        labRemainingTime.setText(timeInterval*60+"");

        //run thread
        timerHandler.removeCallbacks(timerRunnable);
        timerRunnable=new MyTime((TextView) findViewById(R.id.labTime),(TextView) findViewById(R.id.txtRemainigTime),timerHandler,lastTime,timeInterval,(Button)findViewById(R.id.btnTakeDrag),(ProgressBar) findViewById(R.id.timeProgressbar));
        timerHandler.postDelayed(timerRunnable, 60000);

        //set notify service
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
        setProgrss(lastDragTime,timeInterval);


        //update stac
        //get day of week
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        String currentDayOfTheWeek = sdf.format(Calendar.getInstance().getTime());
        //checking should memmory  be reset
        //get current day form memory
        String currentDayFromMemmory=sharedPref.getString("currentDay","");
        if(!currentDayFromMemmory.equals(currentDayOfTheWeek))
        {
            editor.putString("currentDay",currentDayOfTheWeek);
            editor.putInt(currentDayOfTheWeek,0);
        }
        //update drugs number
        int numberOfDrugsToday=sharedPref.getInt(currentDayOfTheWeek,0);
        editor.putInt(currentDayOfTheWeek,numberOfDrugsToday+1);
        editor.commit();


    }

    public void onReset(View view) {
        timerHandler.removeCallbacks(timerRunnable);
        setDragTimeLook();
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.timeProgressbar);
        progressBar.setVisibility(View.GONE);

    }

    public void onStac(View view) {
        Intent intent=new Intent(this, StacActivity.class);
        startActivity(intent);
    }
}


class MyTime extends Thread
{
    TextView labTime;
    TextView labInfoLab;
    Handler timerHandler;
    long lastTime;
    long timeInterval;
    Button takeDragButton;
    ProgressBar progressBar;


    public MyTime(TextView labTime, TextView labInfoLab, Handler timerHandler, long lastTime, int timeInterval, Button takeDragButton,ProgressBar progressBar) {
        this.labTime = labTime;
        this.timerHandler = timerHandler;
        this.timeInterval=timeInterval;
        this.lastTime=lastTime;
        this.labInfoLab=labInfoLab;
        this.takeDragButton=takeDragButton;
        this.progressBar=progressBar;

    }



    @Override
    public void run() {
        super.run();


        if(Utils.isAfterTimeForDrag(timeInterval,Calendar.getInstance().getTimeInMillis(),lastTime))
        {
            labTime.setText(R.string.timeForDrag);
            labTime.setTextColor(Color.GREEN);
            labInfoLab.setText("");
            takeDragButton.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
        else {


            labTime.setText(Utils.getRemaingTime(timeInterval,Calendar.getInstance().getTimeInMillis(),lastTime) + "");

            timerHandler.postDelayed(this, 60000);
            int progress=(int)timeInterval*60-(int)Utils.getRemaingTime(timeInterval,Calendar.getInstance().getTimeInMillis(),lastTime);

            progressBar.setProgress(progress);

        }
    }
}
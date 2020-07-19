package kozakiewicz.szymon.dragtimer;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    int counter;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    MyTime timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        counter=0;
        setContentView(R.layout.activity_main);
        if(isDragTime())
        {
            TextView labTime=(TextView) findViewById(R.id.labTime);
            labTime.setText(R.string.timeForDrag);
            labTime.setTextColor(Color.GREEN);
            ((TextView) findViewById(R.id.txtRemainigTime)).setText("");
            ((Button)findViewById(R.id.btnTakeDrag)).setEnabled(true);
        }
        else {
            //get data
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            long lastDragTime=1;
            lastDragTime=sharedPref.getLong("lastDragTime",lastDragTime);
            int timeInterval=1;
            timeInterval=sharedPref.getInt("hoursNumber",timeInterval);


            //set Time in timeView
            ((TextView) findViewById(R.id.txtRemainigTime)).setText(R.string.remaining_time);

            //set color:
            ((TextView) findViewById(R.id.labTime)).setTextColor(Color.BLUE);
            //run thread
            timerHandler.removeCallbacks(timerRunnable);
            timerRunnable=new MyTime((TextView) findViewById(R.id.labTime),(TextView) findViewById(R.id.txtRemainigTime),timerHandler,lastDragTime,timeInterval,(Button)findViewById(R.id.btnTakeDrag));
            timerHandler.postDelayed(timerRunnable, 1000);

           Button onDragTime=((Button)findViewById(R.id.btnTakeDrag));
           onDragTime.setEnabled(false);


        }


    }

    private boolean isDragTime() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        int timeInterval=1;
        timeInterval=sharedPref.getInt("hoursNumber",timeInterval);
        timeInterval=60*60*1000*timeInterval;
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

        //set color:
        ((TextView) findViewById(R.id.labTime)).setTextColor(Color.BLUE);

        //run thread
        timerHandler.removeCallbacks(timerRunnable);
        timerRunnable=new MyTime((TextView) findViewById(R.id.labTime),(TextView) findViewById(R.id.txtRemainigTime),timerHandler,lastTime,timeInterval,(Button)findViewById(R.id.btnTakeDrag));
        timerHandler.postDelayed(timerRunnable, 1000);



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


    public MyTime(TextView labTime, TextView labInfoLab, Handler timerHandler, long lastTime, int timeInterval, Button takeDragButton) {
        this.labTime = labTime;
        this.timerHandler = timerHandler;
        this.timeInterval=timeInterval*1000*60*60;
        this.lastTime=lastTime;
        this.labInfoLab=labInfoLab;
        this.takeDragButton=takeDragButton;
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
        }
        else {

             labTime.setText(Utils.getRemaingTime(timeInterval,Calendar.getInstance().getTimeInMillis(),lastTime) + "");

            timerHandler.postDelayed(this, 500);
        }
    }
}
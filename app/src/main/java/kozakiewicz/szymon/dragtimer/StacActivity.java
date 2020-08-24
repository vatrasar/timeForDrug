package kozakiewicz.szymon.dragtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kozakiewicz.szymon.dragtimer.room.DragRepository;
import kozakiewicz.szymon.dragtimer.room.Drug;
import kozakiewicz.szymon.dragtimer.room.TakeDrugEvent;

public class StacActivity extends AppCompatActivity {

    Integer position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stac);
        Intent myIntent = getIntent();
        position = myIntent.getIntExtra("position", 1);

        //determine days order
//        int startIndex =0;
//        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
//        {
//            case Calendar.MONDAY:
//                startIndex=0;
//                break;
//            case Calendar.TUESDAY:
//                startIndex=1;
//                break;
//            case Calendar.WEDNESDAY:
//                startIndex=2;
//                break;
//            case Calendar.THURSDAY:
//                startIndex=3;
//                break;
//            case Calendar.FRIDAY:
//                startIndex=4;
//                break;
//            case Calendar.SATURDAY:
//                startIndex=5;
//                break;
//            case Calendar.SUNDAY:
//                startIndex=6;
//                break;
//        }
//        String[]days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};


//        List<String>daysList=new ArrayList<>();
//        //days before current days in week order
//        for(int i=startIndex;i>=0;i--)
//        {
//            daysList.add(days[i]);
//        }
//        //days after current days in week order
//        for(int i=startIndex+1;i<days.length;i++)
//        {
//            daysList.add(days[i]);
//        }


        //get shared preferences instance
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        List<Day>daysList=getDaysBordersList(7);
        //create bar entrys
        List<BarEntry>barEntrys = new ArrayList<>();
        for(int i=0;i<daysList.size();i++)
        {
            DragRepository dragsRepository=new DragRepository(this.getApplication());
            List<Drug>drugsList= dragsRepository.getAllDragsList();
            Day day=daysList.get(i);
            List<TakeDrugEvent>eventsList=dragsRepository.getStacForDrugAndDay(drugsList.get(position),day.dayStart,day.dayEnd);

            barEntrys.add(new BarEntry(i,eventsList.size()));
        }


        BarDataSet dataSet=new BarDataSet(barEntrys, "Liczba leków z ostatnich dni");
        BarData data=new BarData(dataSet);
        BarChart barChart=(BarChart)findViewById(R.id.chart);
        barChart.setData(data);
        barChart.invalidate();
    }

    private List<Day> getDaysBordersList(int daysNumber) {



       Calendar dayInstance=Utils.getEndOfDay();
       List<Day>daysList = new ArrayList<>();;
       for(int i=0;i<daysNumber;i++)
       {
           long daysEnd=dayInstance.getTimeInMillis();
           dayInstance.add(Calendar.DATE,-1);

           long dayStart=dayInstance.getTimeInMillis();
           daysList.add(new Day(dayStart,daysEnd));

       }
       return daysList;
    }
    class Day{
        long dayStart;
        long dayEnd;

        public Day(long dayStart, long dayEnd) {
            this.dayStart = dayStart;
            this.dayEnd = dayEnd;
        }
    }
}
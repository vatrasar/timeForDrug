package kozakiewicz.szymon.dragtimer;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

public class Utils {
    /**
     * @param timeInterval milisecounds
     * @param currentTime milisecounds
     * @param lastDragTime hours
     * @return
     */
    public static boolean isAfterTimeForDrag(long timeInterval,long currentTime,long lastDragTime)
    {
        timeInterval=timeInterval;
        if(timeInterval<(currentTime-lastDragTime))
            return true;
        else
            return false;
    }

    /**
     * @param timeInterval milisecounds
     * @param currentTime milisecounds
     * @param lastDragTime hours
     * @return remaning time in milisecounds
     */
    public static long getRemaingTime(long timeInterval,long currentTime,long lastDragTime)
    {

        long remanigTime=timeInterval-(currentTime-lastDragTime);

        return remanigTime;
    }

    public static long milisecondsToMinutes(long interval)
    {
        return interval/(1000*60);
    }

    public static long milisecondsToSecounds(long interval)
    {
        return interval/(1000);
    }

    public static long getProgressTime(long timeInterval,long currentTime,long lastDragTime) {
        long progressTime=(currentTime-lastDragTime);
        if(timeInterval<progressTime)
            return timeInterval;

        return progressTime;
    }
    public static Long getTimeInterval(int minutesNumber, int hoursNumber,int secoundsNumber) {
        return (long) (1000 * (minutesNumber * 60 + hoursNumber * 60 * 60 + secoundsNumber));
    }

    public static Calendar getEndOfDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Calendar.getInstance().getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal;
    }
}

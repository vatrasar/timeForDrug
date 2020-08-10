package kozakiewicz.szymon.dragtimer;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

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

}

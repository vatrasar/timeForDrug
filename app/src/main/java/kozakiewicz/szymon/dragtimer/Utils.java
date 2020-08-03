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
        timeInterval=60*60*1000*timeInterval;
        if(timeInterval<(currentTime-lastDragTime))
            return true;
        else
            return false;
    }

    /**
     * @param timeInterval milisecounds
     * @param currentTime milisecounds
     * @param lastDragTime hours
     * @return remaning time in minutes
     */
    public static long getRemaingTime(long timeInterval,long currentTime,long lastDragTime)
    {
        timeInterval=timeInterval*60*60*1000;
        long remanigTime=timeInterval-(currentTime-lastDragTime);
        long result= remanigTime/(1000*60);
        return result;
    }

}

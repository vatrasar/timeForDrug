package kozakiewicz.szymon.dragtimer;

public class Utils {
    public static boolean isAfterTimeForDrag(long timeInterval,long currentTime,long lastDragTime)
    {
        timeInterval=60*60*1000*timeInterval;
        if(timeInterval<(currentTime-lastDragTime))
            return true;
        else
            return false;
    }

    /**
     * @param timeInterval
     * @param currentTime
     * @param lastDragTime
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

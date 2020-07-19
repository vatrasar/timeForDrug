package kozakiewicz.szymon.dragtimer;

public class Utils {
    public static boolean isAfterTimeForDrag(long timeInterval,long currentTime,long lastDragTime)
    {
        if(timeInterval<(currentTime-lastDragTime))
            return true;
        else
            return false;
    }

    public static long getRemaingTime(long timeInterval,long currentTime,long lastDragTime)
    {
        long remanigTime=timeInterval-(currentTime-lastDragTime);
        long result= remanigTime/(1000*60);
        return result;
    }
}

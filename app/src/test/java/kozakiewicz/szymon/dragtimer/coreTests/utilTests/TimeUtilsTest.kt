package kozakiewicz.szymon.dragtimer.coreTests.utilTests

import kozakiewicz.szymon.dragtimer.common.util.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimeUtilsTest {

    @Test
    fun isDoseReady_whenIntervalHasElapsed_returnsTrue() {
        val now = 1000000L
        val interval = 60000L
        val lastTaken = now - interval

        val result = TimeUtils.isDoseReady(lastTaken, interval, now)

        assertTrue(result)
    }

    @Test
    fun isDoseReady_whenIntervalHasNotElapsed_returnsFalse() {
        val now = 1000000L
        val interval = 60000L
        val lastTaken = now - (interval / 2)

        val result = TimeUtils.isDoseReady(lastTaken, interval, now)

        assertFalse(result)
    }

    @Test
    fun getRemainingMillis_whenIntervalNotElapsed_returnsCorrectRemainingTime() {
        val now = 1000000L
        val interval = 60000L
        val lastTaken = now - 20000L

        val result = TimeUtils.getRemainingMillis(lastTaken, interval, now)

        assertEquals(40000L, result)
    }

    @Test
    fun getRemainingMillis_whenIntervalHasElapsed_returnsZero() {
        val now = 1000000L
        val interval = 60000L
        val lastTaken = now - 70000L

        val result = TimeUtils.getRemainingMillis(lastTaken, interval, now)

        assertEquals(0L, result)
    }

    @Test
    fun calculateProgress_whenHalfTimeElapsed_returnsHalf() {
        val now = 1000000L
        val interval = 60000L
        val lastTaken = now - 30000L

        val result = TimeUtils.calculateProgress(lastTaken, interval, now)

        assertEquals(0.5f, result, 0.01f)
    }

    @Test
    fun formatRemainingTime_withHoursMinutesSeconds_returnsFormattedString() {
        val millis = (2 * 3600 + 15 * 60 + 30) * 1000L

        val result = TimeUtils.formatRemainingTime(millis)

        assertEquals("02:15:30", result)
    }

    @Test
    fun formatRemainingTime_withOnlyMinutesAndSeconds_returnsFormattedMinutesAndSeconds() {
        val millis = (5 * 60 + 9) * 1000L

        val result = TimeUtils.formatRemainingTime(millis)

        assertEquals("05:09", result)
    }

    @Test
    fun toMillis_withHoursMinutesSeconds_returnsTotalMilliseconds() {
        val result = TimeUtils.toMillis(hours = 1, minutes = 30, seconds = 15)

        assertEquals(5415000L, result)
    }

    @Test
    fun millisToHours_withMilliseconds_returnsHourComponent() {
        val millis = 3600000L * 4

        val result = TimeUtils.millisToHours(millis)

        assertEquals(4, result)
    }
}

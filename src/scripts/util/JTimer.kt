package scripts.util

import java.util.concurrent.TimeUnit

class JTimer(val pattern: String = "%02d:%02d:%02d") {

    val startTime = System.currentTimeMillis()
    val elapsedTime: Long
        get() = System.currentTimeMillis() - startTime

    fun formattedElapsedTime(): String {
        val time = elapsedTime

        val hr = TimeUnit.MILLISECONDS.toHours(time)
        val min = TimeUnit.MILLISECONDS.toMinutes(time)
        val sec = TimeUnit.MILLISECONDS.toSeconds(time)

        return String.format(pattern, hr, min, sec)
    }
}
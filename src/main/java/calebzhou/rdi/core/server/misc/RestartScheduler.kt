package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.utils.ServerUtils
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * Created  on 2022-10-21,14:13.
 */
object RestartScheduler {
    init {
        val task = object :TimerTask(){
            override fun run() {
                RdiCoreServer.server.saveAllChunks(true, true, true)
                System.exit(114514)
            //RdiCoreServer.server.halt(false)
            }
        }
        val now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
        var nextRun = now.withHour(5).withMinute(0).withSecond(0)
        if (now > nextRun)
            nextRun = nextRun.plusDays(1)

        val duration = Duration.between(now, nextRun)
        val initialDelay = duration.seconds

        val scheduler = Executors.newScheduledThreadPool(1)
        scheduler.scheduleAtFixedRate(
            task,
            initialDelay,
            TimeUnit.DAYS.toSeconds(1),
            TimeUnit.SECONDS
        )

    }
}

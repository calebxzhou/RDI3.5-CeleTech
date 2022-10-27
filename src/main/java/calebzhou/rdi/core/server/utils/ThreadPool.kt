package calebzhou.rdi.core.server.utils

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

object ThreadPool {
    //给线程池设定名字
    private fun getPoolNameFactory(poolName: String): ThreadFactory {
        return ThreadFactoryBuilder().setNameFormat("${poolName}-%d").build()
    }
    //创建一个线程池 线程数量是cpu核心数-1
    fun newPool(poolName:String): ExecutorService {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1,getPoolNameFactory(poolName))
    }
    //创建一个单线程池
    fun newSingleThreadPool(poolName: String):ExecutorService{
        return Executors.newSingleThreadExecutor(getPoolNameFactory(poolName))
    }
    private val exe = Executors.newCachedThreadPool()
    fun newThread(runnable: Runnable) {
        exe.execute(runnable)
    }

    fun doAfter(seconds: Int, runnable: Runnable) {
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    runnable.run()
                    cancel()
                }
            },
            seconds * 1000L
        )
    }
}

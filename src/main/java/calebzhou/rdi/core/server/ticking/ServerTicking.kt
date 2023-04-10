package calebzhou.rdi.core.server.ticking

import kotlinx.coroutines.*
import net.minecraft.server.MinecraftServer
import java.util.function.BooleanSupplier

/**
 * Created  on 2022-10-30,19:01.
 */
object ServerTicking {
    @JvmStatic
    fun tick(server: MinecraftServer, hasTimeLeft: BooleanSupplier) {
        runBlocking {
            withTimeoutOrNull(100){
                ticking(server, hasTimeLeft)
            }
        }
    }
    suspend fun ticking(server: MinecraftServer, hasTimeLeft: BooleanSupplier)=
        GlobalScope.launch(Dispatchers.IO){
        server.tickServer(hasTimeLeft);
    }.join()
}

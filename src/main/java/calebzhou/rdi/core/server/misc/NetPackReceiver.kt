package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.RdiCoreServer
import calebzhou.rdi.core.server.RdiMemoryStorage
import calebzhou.rdi.core.server.constant.FileConst
import calebzhou.rdi.core.server.constant.NetworkPackets
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.ServerUtils
import calebzhou.rdi.core.server.utils.ThreadPool
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.level.block.SaplingBlock
import org.apache.commons.io.FileUtils
import org.quiltmc.qsl.networking.api.PacketSender
import org.quiltmc.qsl.networking.api.ServerPlayNetworking
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by calebzhou on 2022-10-15,22:33.
 */
class NetPackReceiver {
    companion object{
        fun register(){
            ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.AFK_DETECT,::afkDetect)
            ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.DANCE_TREE_GROW,::danceTreeGrow)
            ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.SAVE_WORLD,::saveWorld)
            ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.ANIMAL_SEX,::animalSex)
            ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.HW_SPEC,::receiveHardwareSpec)
        }

        private fun receiveHardwareSpec(
            server: MinecraftServer,
            player: ServerPlayer,
            impl: ServerGamePacketListenerImpl,
            buf: FriendlyByteBuf,
            sender: PacketSender
        ) {
            val specJson = buf!!.readUtf()
            ThreadPool.newThread {
                try {
                    val hwSpecFile =
                        File(FileConst.getHwSpecFolder(), player!!.stringUUID + ".txt")
                    if (!hwSpecFile.exists()) {
                        hwSpecFile.createNewFile()
                    }
                    //写入硬件信息
                    FileUtils.write(hwSpecFile, specJson, StandardCharsets.UTF_8)
                } catch (e: IOException) {
                    RdiCoreServer.LOGGER.error(e.stackTrace)
                }
            }

        }

        private fun animalSex(
            server: MinecraftServer,
            player: ServerPlayer,
            impl: ServerGamePacketListenerImpl,
            buf: FriendlyByteBuf,
            sender: PacketSender
        ) {
            val eid = buf.readUtf();
            val entity = player.getLevel().getEntity(UUID.fromString(eid)) ?: return;
            if(entity is AgeableMob){
                if(player.experienceLevel<10){
                    PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_ERROR,"精验不足，需要10");
                    return;
                }
                player.experienceLevel-=10;
                entity.age = 0;
                PlayerUtils.sendChatMessage(player,PlayerUtils.RESPONSE_SUCCESS,"生长成功");
            }
        }

        private fun saveWorld(server: MinecraftServer, player: ServerPlayer, serverGamePacketListenerImpl: ServerGamePacketListenerImpl, buf: FriendlyByteBuf, packetSender: PacketSender) {
            ServerUtils.save()
        }

        private fun danceTreeGrow(
            minecraftServer: MinecraftServer,
            player: ServerPlayer,
            impl: ServerGamePacketListenerImpl,
            buf: FriendlyByteBuf,
            sender: PacketSender
        ) {
            val coord = buf.readUtf()
            val split = coord.split(",")
            val x= Integer.parseInt(split[0])
            val y= Integer.parseInt(split[1])
            val z= Integer.parseInt(split[2])
            val bpos = BlockPos(x,y,z)
            val world = player.getLevel()
            val block = world.getBlockState(bpos).block
            if(block is SaplingBlock){
                ServerUtils.executeOnServerThread{
                    block.performBonemeal(world,player.random,bpos,world.getBlockState(bpos))
                }
            }
        }

        private fun afkDetect(
            minecraftServer: MinecraftServer,
            player: ServerPlayer,
            serverGamePacketListenerImpl: ServerGamePacketListenerImpl,
            buf: FriendlyByteBuf,
            packetSender: PacketSender
        ) {
            try {
                val afkTicks: Int = buf.readInt()
                if (afkTicks > 0) RdiMemoryStorage.afkMap.put(
                    player.scoreboardName,
                    afkTicks
                ) else RdiMemoryStorage.afkMap.removeInt(player.scoreboardName)
            } catch (e: Exception) {
                RdiCoreServer.LOGGER.error(e.stackTrace)
            }
        }




    }

}


//隔空跳跃
/*ServerPlayNetworking.registerGlobalReceiver(NetworkPackets.LEAP,((server, player, handler, buf, responseSender) -> {
    String s = buf.readUtf();
    BlockPos bpos = BlockPos.of(Long.parseLong(s));
    double distance = bpos.distSqr(new Vec3i(player.getX(), player.getY(), player.getZ()));
    int levelNeed = (int) Math.cbrt(distance) / 2;
    if(player.experienceLevel<levelNeed) {
        sendChatMessage(player,"经验不足，需要"+levelNeed+"经验！");
        return;
    }
    PlayerUtils.teleport(player, new PlayerLocation(bpos.offset(0,2,0),player.getLevel(), player.getYRot(), player.getXRot()));
    sendChatMessage(player, PlayerUtils.RESPONSE_SUCCESS,"1");
}));*/

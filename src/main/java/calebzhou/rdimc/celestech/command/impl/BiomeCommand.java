package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.AreaArgCommand;
import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import calebzhou.rdimc.celestech.utils.WorldUtils;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.PalettedContainer;
import org.lwjgl.system.MathUtil;

import static calebzhou.rdimc.celestech.constant.CoordType.*;

public class BiomeCommand extends AreaArgCommand {
    public BiomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    //x1,y1,z1,x2,y2,z2,biome
    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
            super.onExecute(player, arg);
            Biome biome = player.getServer().getRegistryManager().get(Registry.BIOME_KEY).get(new Identifier(nameArg));
            if(biome==null){
                TextUtils.sendChatMessage(player,"生物群系"+nameArg+"不存在 ,请您输入生物群系的标识符。", MessageType.ERROR);
                return;
            }
            Vec3i xyz1=getCoordVec(x1);
            Vec3i xyz2=getCoordVec(x2);
            double dist = Math.sqrt(xyz1.getSquaredDistance(xyz2))*1.5;
            if(!PlayerUtils.checkExpLevel(player,(int)dist)){
                TextUtils.sendChatMessage(player,"变更您指定这一区域的生物群系，需要"+(int)dist+"级经验！", MessageType.ERROR);
                return;
            }
            BlockBox blockBox = new BlockBox(xyz1.getX(),xyz1.getY(),xyz1.getZ(),xyz2.getX(),xyz2.getY(),xyz2.getZ());
            BlockPos.stream(blockBox).forEach(blockPos ->
                WorldUtils.changeBiome(blockPos,player.getWorld(),biome)
            );

            TextUtils.sendChatMessage(player,"成功将您所在区域的生物群系变更为"+nameArg+"，区块重新加载后生效",MessageType.SUCCESS);
    }

}

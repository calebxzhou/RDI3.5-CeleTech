package calebzhou.rdimc.celestech.command.impl;

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

public class BiomeCommand extends OneArgCommand {
    public BiomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    //x1,y1,z1,x2,y2,z2,biome
    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
            String[] split = arg.split(",");
            String biomeName = split[6];
            Biome biome = player.getServer().getRegistryManager().get(Registry.BIOME_KEY).get(new Identifier(biomeName));
            if(biome==null){
                TextUtils.sendChatMessage(player,"生物群系"+biomeName+"不存在 ,请您输入生物群系的标识符。", MessageType.ERROR);
                return;
            }
            Vec3i xyz1=new Vec3i(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]));
            Vec3i xyz2=new Vec3i(Integer.parseInt(split[3]),Integer.parseInt(split[4]),Integer.parseInt(split[5]));
            double dist = Math.sqrt(xyz1.getSquaredDistance(xyz2))*1.5;
            if(!PlayerUtils.checkExpLevel(player,(int)dist)){
                TextUtils.sendChatMessage(player,"变更您指定这一区域的生物群系，需要"+(int)dist+"级经验！", MessageType.ERROR);
                return;
            }
            BlockBox blockBox = new BlockBox(xyz1.getX(),xyz1.getY(),xyz1.getZ(),xyz2.getX(),xyz2.getY(),xyz2.getZ());
            BlockPos.stream(blockBox).forEach(blockPos ->
                WorldUtils.changeBiome(blockPos,player.getWorld(),biome)
            );

            TextUtils.sendChatMessage(player,"成功将您所在区域的生物群系变更为"+biomeName+"，区块重新加载后生效",MessageType.SUCCESS);
    }

/*
 public static Biome adapt(BiomeType biomeType) {
        return FabricWorldEdit.LIFECYCLED_SERVER.valueOrThrow()
            .registryAccess()
            .registryOrThrow(Registry.BIOME_REGISTRY)
            .get(new ResourceLocation(biomeType.getId()));
    }

    public static BiomeType adapt(Biome biome) {
        ResourceLocation id = FabricWorldEdit.LIFECYCLED_SERVER.valueOrThrow().registryAccess()
            .registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        Objects.requireNonNull(id, "biome is not registered");
        return BiomeTypes.get(id.toString());
    }

 @Override
    public boolean setBiome(BlockVector3 position, BiomeType biome) {
        checkNotNull(position);
        checkNotNull(biome);

        ChunkAccess chunk = getWorld().getChunk(position.getBlockX() >> 4, position.getBlockZ() >> 4);
        PalettedContainer<Biome> biomeArray = chunk.getSection(chunk.getSectionIndex(position.getY())).getBiomes();
        biomeArray.getAndSetUnchecked(
            position.getX() & 3, position.getY() & 3, position.getZ() & 3,
            FabricAdapter.adapt(biome)
        );
        chunk.setUnsaved(true);
        return true;
    }

 */
}

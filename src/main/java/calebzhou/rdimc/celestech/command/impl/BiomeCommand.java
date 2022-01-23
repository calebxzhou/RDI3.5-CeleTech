package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.OneArgCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.PalettedContainer;

public class BiomeCommand extends OneArgCommand {
    public BiomeCommand(String name, int permissionLevel) {
        super(name, permissionLevel);
    }

    @Override
    protected void onExecute(ServerPlayerEntity player, String arg) {
        if(!PlayerUtils.checkExpLevel(player,10)){
            TextUtils.sendChatMessage(player,"变更您所在区块的生物群系需要10级经验！", MessageType.ERROR);
            return;
        }
        Chunk chunk = player.getWorld().getChunk(player.getBlockX()>>4,player.getBlockZ()>>4);
        PalettedContainer<Biome> biomeArray = chunk.getSection(player.getBlockY()).getBiomeContainer();
        Biome biome = player.getServer().getRegistryManager().get(Registry.BIOME_KEY).get(new Identifier(arg));
        if(biome==null)
            TextUtils.sendChatMessage(player,"生物群系"+arg+"不存在"+"，请您输入生物群系的标识符。", MessageType.ERROR);
        biomeArray.swap(player.getBlockX(),player.getBlockY(),player.getBlockZ(), biome);
        chunk.setShouldSave(true);
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

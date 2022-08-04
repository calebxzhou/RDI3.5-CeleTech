package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class BioCommand implements RdiCommand {
  /*  private static final SuggestionProvider<CommandSourceStack> SUGGEST_TEMPLATES = (commandContext, suggestionsBuilder) ->
            SharedSuggestionProvider.suggestResource(commandContext.getSource().getLevel().getBiomeManager().listTemplates(), suggestionsBuilder);
*/
    @Override
    public String getName() {
        return "bio";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return Commands.literal(getName())
                .then(Commands.argument("生物群系的命名空间", StringArgumentType.string())
                        .then(Commands.argument("x1", IntegerArgumentType.integer())
                                .then(Commands.argument("y1", IntegerArgumentType.integer())
                                        .then(Commands.argument("z1", IntegerArgumentType.integer())
                                                .then(Commands.argument("x2", IntegerArgumentType.integer())
                                                        .then(Commands.argument("y2", IntegerArgumentType.integer())
                                                                .then(Commands.argument("z2", IntegerArgumentType.integer())
                                                                        .executes(context -> exec(
                                                                                context.getSource().getPlayer(),
                                                                                StringArgumentType.getString(context,"生物群系的命名空间"),
                                                                                IntegerArgumentType.getInteger(context,"x1"),
                                                                                IntegerArgumentType.getInteger(context,"y1"),
                                                                                IntegerArgumentType.getInteger(context,"z1"),
                                                                                IntegerArgumentType.getInteger(context,"x2"),
                                                                                IntegerArgumentType.getInteger(context,"y2"),
                                                                                IntegerArgumentType.getInteger(context,"z2")

                                                                                )
                                                                        )
                                                                )
                                                        )

                                                )
                                        )
                                )
                        )
                )
                ;
    }

    private int exec(ServerPlayer player, String biome,int x1,int y1,int z1,int x2,int y2,int z2) {
        Registry<Biome> biomeRegistry = player.getLevel().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
        ResourceLocation biomeResLoca = new ResourceLocation(biome);
        Biome bio = biomeRegistry.get(biomeResLoca);
        ResourceKey<Biome> biomeResourceKey = biomeRegistry.getResourceKey(bio).get();
        Holder<Biome> biomeHolder = biomeRegistry.getHolder(biomeResourceKey).get();
        if(bio == null){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"生物群系"+biome+"不存在 ,请您输入生物群系的标识符。详见https://minecraft.fandom.com/wiki/Biome");
            return 1;
        }
        int dx = Math.abs(x2-x1);
        int dy = Math.abs(y2-y1);
        int dz = Math.abs(z2-z1);
        int expNeed = 2*dx*dy*dz;
        if(player.experienceLevel<expNeed){
            TextUtils.sendChatMessage(player,MessageType.ERROR,"您的经验不足"+expNeed+"级，无法更改！");
            return 1;
        }


        BoundingBox boundingBox = new BoundingBox(x1, y1, x1, x2, y2, z2);
        BlockPos.betweenClosedStream(boundingBox).forEach(blockPos->{
            ChunkAccess chunk = player.getLevel().getChunk(blockPos);
            int sectionYindex = (blockPos.getY()+64) / 16;
            PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) chunk.getSection(sectionYindex).getBiomes();
            int mx=blockPos.getX()&3;
            int my=blockPos.getY()&3;
            int mz=blockPos.getZ()&3;
            biomes.getAndSet(mx,my,mz, biomeHolder);
            chunk.setUnsaved(true);
        });
        player.experienceLevel-=expNeed;
        TextUtils.sendChatMessage(player,MessageType.SUCCESS,"成功！重载区块后生效。");
        return 1;
    }
    /*public static void changeBiome(BlockPos bpos,ServerLevel world,Biome biome){
        ChunkAccess chunk = world.getChunk(bpos.getX()>>4,bpos.getZ()>>4);
        int sectionYindex = (bpos.getY()+64)>>4;
        PalettedContainer<Holder<Biome>> biomeArray = (PalettedContainer<Holder<Biome>>) chunk.getSection(sectionYindex).getBiomes();
        int mx=bpos.getX()&3;
        int my=bpos.getY()&3;
        int mz=bpos.getZ()&3;
        biomeArray.getAndSet(mx,my,mz, (biome));
        chunk.setUnsaved(true);
    }*/
}

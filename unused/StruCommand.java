package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.RdiCommand;
import calebzhou.rdimc.celestech.constant.MessageType;
import calebzhou.rdimc.celestech.utils.ServerUtils;
import calebzhou.rdimc.celestech.utils.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class StruCommand extends RdiCommand {
    static class RdiStructure{
        public RdiStructure(String id, int xpNeed, String spawner) {
            this.id = id;
            this.xpNeed = xpNeed;
            this.spawner = spawner;
        }

        String id;
        int xpNeed;
        String spawner;
    }
    private static final List<RdiStructure> availableStructureList = new ReferenceArrayList<>();
    static {
        availableStructureList.add(new RdiStructure("swamp_hut",250,"witch"));
        availableStructureList.add(new RdiStructure("fortress",190,"blaze"));
        availableStructureList.add(new RdiStructure("monument",100,"elder_guardian"));
        availableStructureList.add(new RdiStructure("pillager_outpost",160,"pillager"));
        availableStructureList.add(new RdiStructure("ocean_ruin",300,"drowned"));
        availableStructureList.add(new RdiStructure("bastion_remnant",170,"magma_cube"));

    }
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_TEMPLATES = (commandContext, suggestionsBuilder) ->
            SharedSuggestionProvider.suggest(availableStructureList.stream().map(stru-> stru.id), suggestionsBuilder);
    @Override
    public String getName() {
        return "stru";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getExecution() {
        return baseArgBuilder
                .then(Commands.argument("生成结构的命名空间", StringArgumentType.string()).suggests(SUGGEST_TEMPLATES)
                        .executes(context -> exec(context.getSource().getPlayer(),StringArgumentType.getString(context, "生成结构的命名空间")))
                );
    }

    private int exec(ServerPlayer player, String namespc) {
        BlockPos bpos = player.blockPosition();
        RdiStructure rs = availableStructureList.stream().filter(stru -> stru.id.equals(namespc)).findFirst().get();
        if(player.experienceLevel<rs.xpNeed){
            TextUtils.sendChatMessage(player, MessageType.ERROR,"经验不足，需要"+rs.xpNeed+"经验");
            return 1;
        }

        ServerUtils.executeCommandOnServer(String.format("setblock %s %s %s spawner{SpawnData:{entity:{id:%s}},Delay:599} replace",bpos.getX(),bpos.getY(),bpos.getZ(),rs.spawner));
        player.experienceLevel-=rs.xpNeed;
        TextUtils.sendChatMessage(player,"成功将结构"+rs.id+"写入您所在的区块"+player.getLevel().getChunk(bpos).getPos());
        return 1;
    }
}

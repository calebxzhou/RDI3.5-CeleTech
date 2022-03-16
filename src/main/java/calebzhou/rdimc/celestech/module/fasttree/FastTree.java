package calebzhou.rdimc.celestech.module.fasttree;

import calebzhou.rdimc.celestech.api.NetworkReceivableC2S;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class FastTree implements NetworkReceivableC2S {
    public static final ResourceLocation FAST_TREE_NETWORK =new ResourceLocation(MODID,"fast_tree");

    public FastTree() {

    }

    public void growTree(ServerPlayer player, BlockPos blockPos){
        ServerLevel world = player.getLevel();
        Block block = world.getBlockState(blockPos).getBlock();
        if(block instanceof SaplingBlock saplingBlock){
            saplingBlock.performBonemeal(world,player.getRandom(),blockPos,world.getBlockState(blockPos));
        }
    }

    @Override
    public void registerNetworking() {
        ServerPlayNetworking.registerGlobalReceiver(FAST_TREE_NETWORK,((server, player, handler, buf, responseSender) -> {
            String s = buf.readUtf();
            BlockPos bpos = BlockPos.of(Long.parseLong(s));
            growTree(player,bpos);
        }));
    }
}

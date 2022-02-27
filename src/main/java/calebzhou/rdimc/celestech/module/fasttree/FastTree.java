package calebzhou.rdimc.celestech.module.fasttree;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static calebzhou.rdimc.celestech.RDICeleTech.MODID;

public class FastTree {
    public static final Identifier FAST_TREE_NETWORK =new Identifier(MODID,"fast_tree");

    public FastTree() {
        ServerPlayNetworking.registerGlobalReceiver(FAST_TREE_NETWORK,((server, player, handler, buf, responseSender) -> {
            String s = buf.readString();
            BlockPos bpos = BlockPos.fromLong(Long.parseLong(s));
            growTree(player,bpos);
        }));
    }

    public void growTree(ServerPlayerEntity player, BlockPos blockPos){
        ServerWorld world = player.getWorld();
        Block block = world.getBlockState(blockPos).getBlock();
        if(block instanceof SaplingBlock saplingBlock){
            saplingBlock.grow(world,player.getRandom(),blockPos,world.getBlockState(blockPos));
        }
    }
}

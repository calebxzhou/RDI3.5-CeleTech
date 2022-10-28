package calebzhou.rdi.core.server.module;

import calebzhou.rdi.core.server.RdiCoreServer;
import calebzhou.rdi.core.server.utils.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.stream.IntStream;

//死亡物品随机掉落
public class DeathRandomDrop {
    public static void handleDeath(ServerPlayer player){
        int dropSlotAmount;
        if(PlayerUtils.INSTANCE.isInTheEnd(player)){
            dropSlotAmount=35;
        }else if(PlayerUtils.INSTANCE.isInNether(player)){
            dropSlotAmount=25;
        }else{
            dropSlotAmount=7;
        }
        //boolean isSetChestSuccessful;
        Level world=player.getLevel();
        /*BlockPos chestPos = player.blockPosition();
        if(new BoundingBox(-100,-64,-100,100,320,100).isInside(new Vec3i(chestPos.getX(), chestPos.getY(), chestPos.getZ())))
            isSetChestSuccessful = false;
        else
            isSetChestSuccessful = world.setBlockAndUpdate(chestPos, Blocks.CHEST.defaultBlockState());*/
        ArrayList<ItemStack> dropItemList = new ArrayList<>();
        IntStream
                .range(0, dropSlotAmount)
                .map(i -> RdiCoreServer.RANDOM.nextInt(1, 35))
                .mapToObj(ran -> player.getInventory().getItem(ran))
                .filter(stack2Drop -> !stack2Drop.isEmpty() && stack2Drop.getItem() != Items.AIR)
                .forEach(stack2Drop -> {
                    //没有标签的东西才会掉落
                    if(!stack2Drop.hasTag()){
                        dropItemList.add(stack2Drop);
                        player.getInventory().removeItem(stack2Drop);
                    }
        });

            dropItemList.forEach(stack2Drop -> world.addFreshEntity(new ItemEntity(world,player.getX()+0.5f,
                    player.getY()+1.1f,player.getZ()+0.5f,
                    stack2Drop)));
    }
}

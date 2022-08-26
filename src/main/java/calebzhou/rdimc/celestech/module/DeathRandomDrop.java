package calebzhou.rdimc.celestech.module;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.utils.PlayerUtils;
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
        if(PlayerUtils.getDimensionName(player).equals("minecraft:the_end")){
            dropSlotAmount=35;
        }else if(PlayerUtils.getDimensionName(player).equals("minecraft:the_nether")){
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
                .map(i -> RDICeleTech.RANDOM.nextInt(1, 35))
                .mapToObj(ran -> player.getInventory().getItem(ran))
                .filter(stack2Drop -> !stack2Drop.isEmpty() && stack2Drop.getItem() != Items.AIR)
                .forEach(stack2Drop -> {
                    //没有标签的东西才会掉落
                    if(!stack2Drop.hasTag()){
                        dropItemList.add(stack2Drop);
                        player.getInventory().removeItem(stack2Drop);
                    }
        });
       /* BlockEntity chest = world.getBlockEntity(chestPos);

        if(chest instanceof ChestBlockEntity chs && chest!=null){
            isSetChestSuccessful = true;
            for (int i = 0; i < dropItemList.size(); i++) {
                chs.setItem(i,dropItemList.get(i));
            }
        }else{
            isSetChestSuccessful = false;
        }

        if(!isSetChestSuccessful){*/
            dropItemList.forEach(stack2Drop -> world.addFreshEntity(new ItemEntity(world,player.getX()+0.5f,
                    player.getY()+1.1f,player.getZ()+0.5f,
                    stack2Drop)));
       // }
    }
}

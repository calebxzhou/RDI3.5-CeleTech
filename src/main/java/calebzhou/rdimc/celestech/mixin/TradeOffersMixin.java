package calebzhou.rdimc.celestech.mixin;

import calebzhou.rdimc.celestech.RDICeleTech;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffers.class)
public class TradeOffersMixin {
    @Shadow
    @Mutable
    public static Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_TRADES;
    static {
        int cheapPrice = 1;
        int price = 3;
        int expensivePrice=8;
        int count = 1;
        int manyCount = 4;
        int maxUses= 4;
        int experience = 5;
        WANDERING_TRADER_TRADES = RDICeleTech.copyToFastUtilMap(
                ImmutableMap.of(1, new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Items.SEA_PICKLE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.SLIME_BALL, expensivePrice, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.GLOWSTONE, price, manyCount, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.NAUTILUS_SHELL, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.FERN, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.SUGAR_CANE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PUMPKIN, cheapPrice, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.KELP, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.CACTUS, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.DANDELION, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.POPPY, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BLUE_ORCHID, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.ALLIUM, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.AZURE_BLUET, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.RED_TULIP, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.ORANGE_TULIP, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.WHITE_TULIP, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PINK_TULIP, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.OXEYE_DAISY, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.CORNFLOWER, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.LILY_OF_THE_VALLEY, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.WHEAT_SEEDS, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BEETROOT_SEEDS, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PUMPKIN_SEEDS, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.MELON_SEEDS, price, count, maxUses, experience),
                //树苗
                        new TradeOffers.SellItemFactory(Items.ACACIA_SAPLING, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BIRCH_SAPLING, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.DARK_OAK_SAPLING, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.JUNGLE_SAPLING, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.OAK_SAPLING, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.SPRUCE_SAPLING, price, count, maxUses, experience),
                //染料
                        new TradeOffers.SellItemFactory(Items.RED_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.WHITE_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BLUE_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PINK_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BLACK_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.GREEN_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.MAGENTA_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.YELLOW_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.GRAY_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PURPLE_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.LIME_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.ORANGE_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BROWN_DYE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.CYAN_DYE, price, count, maxUses, experience),
                //珊瑚
                        new TradeOffers.SellItemFactory(Items.BRAIN_CORAL_BLOCK, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BUBBLE_CORAL_BLOCK, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.FIRE_CORAL_BLOCK, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.HORN_CORAL_BLOCK, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.TUBE_CORAL_BLOCK, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.VINE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BROWN_MUSHROOM, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.RED_MUSHROOM, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.LILY_PAD, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.SMALL_DRIPLEAF, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.SAND, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.RED_SAND, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.POINTED_DRIPSTONE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.ROOTED_DIRT, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.MOSS_BLOCK, price, count, maxUses, experience)
                        }, 2,
                new TradeOffers.Factory[]{
                        new TradeOffers.SellItemFactory(Items.TROPICAL_FISH_BUCKET, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PUFFERFISH_BUCKET, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PACKED_ICE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.BLUE_ICE, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.GUNPOWDER, price, count, maxUses, experience),
                        new TradeOffers.SellItemFactory(Items.PODZOL, price, count, maxUses, experience)
        }));
    }
}

package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WorkManager {

    public static float getSpeedValue(MinerBlockEntity miner) {
        var drillCountMod = 0.5f * miner.drillCount;
        var product = miner.getProduct(false).getItem();
        return UpgradeManager.getSpeedPerDrill(miner.drill.item) * drillCountMod / WorkManager.getSpeedMod(product);
    }

    public static int getSpeedMod(Item item) {
        if (item == null)
            return 1;
        ItemStack stack = new ItemStack(item);
        if (stack.is(ItemTags.DIAMOND_ORES))
            return 5;
        if (stack.is(ItemTags.EMERALD_ORES))
            return 6;
        if (item.equals(Items.OBSIDIAN))
            return 10;
        if (item.equals(Items.ANCIENT_DEBRIS))
            return 20;

        return 1;
    }

    public static int getOutputSize(Item item) {
        if (item == null)
            return 0;

        if (item.equals(Items.STONE) || item.equals(Items.DIORITE) || item.equals(Items.GRANITE)
                || item.equals(Items.ANDESITE) || item.equals(Items.NETHERRACK))
            return 8;

        if (item.equals(Items.COAL_ORE) || item.equals(Items.DEEPSLATE_COAL_ORE)
                || item.equals(Items.COPPER_ORE) || item.equals(Items.DEEPSLATE_COPPER_ORE)
                || item.equals(Items.NETHER_QUARTZ_ORE))
            return 2;

        return 1;
    }
}

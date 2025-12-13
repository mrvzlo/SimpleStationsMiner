package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WorkManager {
    public static boolean getWorking(MinerBlockEntity miner) {
        if (miner.type == null || miner.speed == 0)
            return false;
        if (Config.isExtendedMod() && (miner.coolant < 1 || miner.redstone < 1))
            return false;
        if (miner.fuel.getEnergyStored() < Config.ENERGY_PER_TICK.get())
            return false;
        if (!miner.isValidWorld())
            return false;

        ItemStack outSlot = miner.inventory.getStackInSlot(ModContainer.OUTPUT_SLOT);
        if (outSlot.getCount() == 0)
            return true;
        if (outSlot.getCount() + miner.outputSize > outSlot.getMaxStackSize())
            return false;
        return outSlot.getItem().equals(miner.type);
    }

    public static void performWorkTick(MinerBlockEntity miner) {
        miner.progress += miner.speed;
        var fuelUsage = Config.ENERGY_PER_TICK.get() * miner.powerConsuption;
        miner.fuel.extractEnergy(fuelUsage, false);
        SoundManager.playSound(miner, miner.speed);
    }

    public static void performWorkEnd(MinerBlockEntity miner) {
        if (Config.isExtendedMod()) {
            miner.coolant--;
            miner.redstone--;
        }

        var outSlot = miner.inventory.getStackInSlot(ModContainer.OUTPUT_SLOT);
        var toAdd = new ItemStack(miner.type);
        toAdd.setCount(outSlot.getCount() + miner.outputSize);
        miner.inventory.setStackInSlot(ModContainer.OUTPUT_SLOT, toAdd);
    }

    public static int getOutputSize(Item item) {
        if (item == null)
            return 1;

        if (item.equals(Items.STONE) || item.equals(Items.DIORITE) || item.equals(Items.GRANITE)
                || item.equals(Items.ANDESITE))
            return 8;

        if (item.equals(Items.COAL_ORE) || item.equals(Items.DEEPSLATE_COAL_ORE)
                || item.equals(Items.COPPER_ORE) || item.equals(Items.DEEPSLATE_COPPER_ORE)
                || item.equals(Items.NETHER_QUARTZ_ORE))
            return 2;

        return 1;
    }

    public static float getSpeedValue(MinerBlockEntity miner) {
        var drillCountMod = 0.5f * miner.drillCount;
        return UpgradeManager.getSpeedPerDrill(miner.drill) * drillCountMod / WorkManager.getSpeedMod(miner.type);
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

}

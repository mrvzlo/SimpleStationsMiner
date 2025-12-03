package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ResourceManager {

    public static void checkAllResources(MinerBlockEntity miner) {
        checkResource(miner, ModContainer.FUEL_SLOT, Items.COAL_BLOCK, Config.FUEL_PER_COAL.get(),
                Config.FUEL_CAPACITY.get(),
                ResourceType.FUEL);

        if (Config.isExtendedMod()) {
            checkResource(miner, ModContainer.REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, Config.MAX_CATALYST.get(),
                    ResourceType.REDSTONE);
            checkResource(miner, ModContainer.COOLANT_SLOT, Items.LAPIS_BLOCK, 1, Config.MAX_COOLANT.get(),
                    ResourceType.COOLANT);
        }
    }

    private static void checkResource(MinerBlockEntity miner, int slot, Item blockItem, int singleValue,
            int maxCapacity, ResourceType type) {
        ItemStack stack = miner.inventory.getStackInSlot(slot);
        int increment = stack.getItem().equals(blockItem) ? singleValue * 9 : singleValue;

        if (stack.isEmpty() || getResourceValue(miner, type) + increment > maxCapacity)
            return;

        stack.shrink(1);
        miner.inventory.setStackInSlot(slot, stack);
        addResource(miner, type, increment);
    }

    private static void addResource(MinerBlockEntity miner, ResourceType type, int amount) {
        switch (type) {
            case FUEL -> miner.fuel.receiveEnergy(amount, false);
            case COOLANT -> miner.coolant += amount;
            case REDSTONE -> miner.redstone += amount;
        }
    }

    private static int getResourceValue(MinerBlockEntity miner, ResourceType type) {
        return switch (type) {
            case FUEL -> miner.fuel.getEnergyStored();
            case COOLANT -> miner.coolant;
            case REDSTONE -> miner.redstone;
        };
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}

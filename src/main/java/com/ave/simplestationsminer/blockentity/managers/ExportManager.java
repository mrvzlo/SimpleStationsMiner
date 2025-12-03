package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class ExportManager {
    public static void pushOutput(MinerBlockEntity miner) {
        ItemStack stack = miner.inventory.getStackInSlot(ModContainer.OUTPUT_SLOT);
        if (stack.isEmpty())
            return;

        BlockPos belowPos = miner.getBlockPos().below();
        IItemHandler handler = null;

        for (int dz = -1; dz <= 1; dz++)
            for (int dx = -1; dx <= 1; dx++) {
                if (handler != null)
                    break;
                var pos = belowPos.offset(dx, 0, dz);
                handler = Capabilities.ItemHandler.BLOCK.getCapability(miner.getLevel(), pos, null, null,
                        Direction.UP);
            }

        if (handler == null)
            return;

        ItemStack remaining = ItemHandlerHelper.insertItem(handler, stack, false);
        miner.inventory.setStackInSlot(ModContainer.OUTPUT_SLOT, remaining);
    }
}

package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

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
                var be = miner.getLevel().getBlockEntity(pos);
                if (be == null)
                    continue;
                var cap = be.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
                if (cap.isPresent())
                    handler = cap.orElse(null);
            }

        if (handler == null)
            return;

        ItemStack remaining = ItemHandlerHelper.insertItem(handler, stack, false);
        miner.inventory.setStackInSlot(ModContainer.OUTPUT_SLOT, remaining);
    }
}

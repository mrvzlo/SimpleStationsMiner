package com.ave.simplestationsminer.blockentity.handlers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class OutputItemHandler implements IItemHandler {
    private final SidedItemHandler parent;

    public OutputItemHandler(SidedItemHandler parent) {
        this.parent = parent;
    }

    public int getSlots() {
        return parent.getSlots();
    }

    public ItemStack getStackInSlot(int slot) {
        return parent.getStackInSlot(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != MinerBlockEntity.OUTPUT_SLOT)
            return ItemStack.EMPTY;
        return parent.extractItem(slot, amount, simulate);
    }

    public int getSlotLimit(int slot) {
        return parent.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return parent.isItemValid(slot, stack);
    }
}

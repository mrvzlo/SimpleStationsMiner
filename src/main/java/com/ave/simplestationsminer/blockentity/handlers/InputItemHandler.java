package com.ave.simplestationsminer.blockentity.handlers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InputItemHandler implements IItemHandler {
    private final SidedItemHandler parent;

    public InputItemHandler(SidedItemHandler parent) {
        this.parent = parent;
    }

    public int getSlots() {
        return parent.getSlots();
    }

    public ItemStack getStackInSlot(int slot) {
        return parent.getStackInSlot(slot);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        // Нельзя класть в выход
        if (slot == MinerBlockEntity.OUTPUT_SLOT)
            return stack;
        return parent.insertItem(slot, stack, simulate);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // input-хендлер ничего не отдаёт
        return ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return parent.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return parent.isItemValid(slot, stack);
    }
}

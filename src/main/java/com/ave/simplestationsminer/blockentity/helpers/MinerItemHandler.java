package com.ave.simplestationsminer.blockentity.helpers;

import com.ave.simplestationsminer.Registrations;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.datagen.ModTags;
import com.ave.simplestationscore.handlers.CommonItemHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MinerItemHandler extends CommonItemHandler {
    public MinerItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == MinerBlockEntity.COOLANT_SLOT)
            return stack.getItem() == Items.LAPIS_BLOCK || stack.getItem() == Items.LAPIS_LAZULI;
        if (slot == MinerBlockEntity.REDSTONE_SLOT)
            return stack.getItem() == Items.REDSTONE_BLOCK || stack.getItem() == Items.REDSTONE;

        if (slot == MinerBlockEntity.TYPE_SLOT)
            return stack.is(ModTags.Items.MINEABLE_TAG);

        if (slot == MinerBlockEntity.PORTAL_SLOT)
            return stack.getItem().equals(Registrations.PORTAL.get());

        if (slot == MinerBlockEntity.DRILL_SLOT)
            return stack.getItem().equals(Registrations.DRILL_ITEM.get()) ||
                    stack.getItem().equals(Registrations.DRILL_ITEM_2.get()) ||
                    stack.getItem().equals(Registrations.DRILL_ITEM_3.get());

        return super.isItemValid(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == MinerBlockEntity.TYPE_SLOT || slot == MinerBlockEntity.PORTAL_SLOT)
            return 1;
        if (slot == MinerBlockEntity.DRILL_SLOT)
            return 2;
        return super.getSlotLimit(slot);
    }
}
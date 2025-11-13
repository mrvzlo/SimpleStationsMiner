package com.ave.smartminer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AutoMinerContainer extends BaseContainerBlockEntity {
    public static final int SIZE = 3;
    private NonNullList<ItemStack> items;

    public AutoMinerContainer(BlockEntityType<AutoMinerBlockEntity> entity, BlockPos pos, BlockState state, int size) {
        super(entity, pos, state);
        items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    // The getter for our item stack list.
    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    // The setter for our item stack list.
    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    // The display name of the menu. Don't forget to add a translation!
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.smartminer.iron_miner");
    }

    // The menu to create from this container. See below for what to return here.
    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }
}

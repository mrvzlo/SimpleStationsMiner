package com.ave.smartminer.blockentity;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    private int progress = 0;
    private static final int MAX_PROGRESS = 100;
    public SmartMinerType type;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    public void tick() {
        if (level.isClientSide || type == null || type == SmartMinerType.Unknown)
            return;

        ItemStack slot = inventory.getStackInSlot(1);
        boolean working = slot.getCount() < slot.getMaxStackSize();
        setWorking(working);
        if (!working)
            return;

        progress++;
        if (progress < MAX_PROGRESS)
            return;

        progress = 0;
        ItemStack toAdd = new ItemStack(type.minedItem);
        toAdd.setCount(slot.getCount() + 16);
        inventory.setStackInSlot(0, toAdd);

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        SmartMiner.LOGGER.info("Added");
    }

    public void setWorking(boolean working) {
        BlockState state = level.getBlockState(worldPosition);
        if (state.hasProperty(SmartMinerBlock.WORKING) && state.getValue(SmartMinerBlock.WORKING) == working)
            return;
        level.setBlock(worldPosition, state.setValue(SmartMinerBlock.WORKING, working), 3);
    }
}

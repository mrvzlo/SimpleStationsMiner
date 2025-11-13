package com.ave.smartminer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AutoMinerBlockEntity extends AutoMinerContainer {
    private int progress = 0;
    private static final int MAX_PROGRESS = 10; // каждые 5 секунд при 20 TPS
    public AutoMinerType type;

    public AutoMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 1);
    }

    public void tick() {
        if (level.isClientSide || type == null || type == AutoMinerType.Unknown)
            return;

        ItemStack slot = this.getItem(0);
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
        this.setItem(0, toAdd);

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        SmartMiner.LOGGER.info("Added");
    }

    public void setWorking(boolean working) {
        BlockState state = level.getBlockState(worldPosition);
        if (state.hasProperty(AutoMinerBlock.WORKING) && state.getValue(AutoMinerBlock.WORKING) == working)
            return;
        level.setBlock(worldPosition, state.setValue(AutoMinerBlock.WORKING, working), 3);
    }
}

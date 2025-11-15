package com.ave.smartminer.blockentity;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    private int progress = 0;
    private static final int MAX_PROGRESS = 10;
    private static final int INCREMENT = 1;
    public SmartMinerType type;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    public void tick() {
        if (level.isClientSide)
            return;

        checkNewType();
        if (type == null || type == SmartMinerType.Unknown)
            return;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        boolean working = slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type.minedItem);
        setWorking(working);
        if (!working)
            return;

        progress++;
        if (progress < MAX_PROGRESS)
            return;

        progress = 0;
        ItemStack toAdd = new ItemStack(type.minedItem);
        toAdd.setCount(slot.getCount() + INCREMENT);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        SmartMiner.LOGGER.info("Added");
    }

    private void setWorking(boolean working) {
        BlockState state = level.getBlockState(worldPosition);
        if (state.hasProperty(SmartMinerBlock.WORKING) && state.getValue(SmartMinerBlock.WORKING) == working)
            return;
        level.setBlock(worldPosition, state.setValue(SmartMinerBlock.WORKING, working), 3);
    }

    private void checkNewType() {
        Item slot = inventory.getStackInSlot(2).getItem();
        SmartMinerType newType = getType(slot);
        if (type == newType)
            return;

        type = newType;
        BlockState state = level.getBlockState(worldPosition);
        level.setBlock(worldPosition, state.setValue(SmartMinerBlock.TYPE, type), 3);
        SmartMiner.LOGGER.info("Changing type to " + type.getSerializedName());
        progress = 0;
    }

    private SmartMinerType getType(Item item) {
        for (SmartMinerType t : SmartMinerType.values())
            if (t.minedItem == item)
                return t;
        return SmartMinerType.Unknown;
    }
}

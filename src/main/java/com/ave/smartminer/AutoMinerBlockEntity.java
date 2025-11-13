package com.ave.smartminer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AutoMinerBlockEntity extends BlockEntity {
    private int progress = 0;
    private static final int MAX_PROGRESS = 100; // каждые 5 секунд при 20 TPS
    private final ItemStackHandler inventory = new ItemStackHandler(1); // 1 слот

    public AutoMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level.isClientSide)
            return;

        ItemStack slot = inventory.getStackInSlot(0);
        if (!slot.isEmpty()) {
            SmartMiner.LOGGER.info("Slot is full");
            return; // слот занят
        }

        SmartMiner.LOGGER.info(this.progress + "");
        progress++;
        if (progress < MAX_PROGRESS)
            return;

        SmartMiner.LOGGER.info("YES");
        progress = 0;
        ItemStack stack = new ItemStack(Items.RAW_IRON);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        inventory.insertItem(0, stack, false);
        setChanged(); // помечаем BlockEntity как изменённый
        SmartMiner.LOGGER.info("CHANGED");
        SmartMiner.LOGGER.info(progress + "");
    }
}

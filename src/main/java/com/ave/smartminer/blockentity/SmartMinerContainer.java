package com.ave.smartminer.blockentity;

import com.ave.smartminer.screen.SmartMinerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SmartMinerContainer extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory;

    public SmartMinerContainer(BlockEntityType<SmartMinerBlockEntity> entity, BlockPos pos, BlockState state,
            int size) {
        super(entity, pos, state);
        inventory = new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.smartminer.iron_miner");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SmartMinerMenu(containerId, inventory, this);
    }
}

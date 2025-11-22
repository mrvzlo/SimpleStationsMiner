package com.ave.simplestationsminer.screen;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.MinerPropertyDelegate;
import com.ave.simplestationsminer.blockentity.handlers.MinerSidedInventory;
import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.uihelpers.UIBlocks;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class MinerScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    public MinerPropertyDelegate props;
    public final MinerBlockEntity miner;

    public MinerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {

        this(syncId, playerInventory, playerInventory.player.getEntityWorld().getBlockEntity(pos),
                ((MinerBlockEntity) playerInventory.player.getEntityWorld().getBlockEntity(pos)).props);
    }

    public MinerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, MinerPropertyDelegate props) {
        super(Registrations.MINER_SCREEN_HANDLER, syncId);
        this.props = props;
        this.inventory = (Inventory) blockEntity;
        this.miner = (MinerBlockEntity) blockEntity;

        addSlot(new Slot(inventory, MinerBlockEntity.TYPE_SLOT, UIBlocks.FILTER_SLOT.left, UIBlocks.FILTER_SLOT.top) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return MinerSidedInventory.canInsert(MinerBlockEntity.TYPE_SLOT, stack);
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.OUTPUT_SLOT, UIBlocks.OUT_SLOT.left, UIBlocks.OUT_SLOT.top) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.FUEL_SLOT, UIBlocks.FUEL_SLOT.left, UIBlocks.FUEL_SLOT.top) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return MinerSidedInventory.canInsert(MinerBlockEntity.FUEL_SLOT, stack);
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.COOLANT_SLOT, UIBlocks.COOL_SLOT.left, UIBlocks.COOL_SLOT.top) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return MinerSidedInventory.canInsert(MinerBlockEntity.COOLANT_SLOT, stack);
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.REDSTONE_SLOT, UIBlocks.CATA_SLOT.left, UIBlocks.CATA_SLOT.top) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return MinerSidedInventory.canInsert(MinerBlockEntity.REDSTONE_SLOT, stack);
            }
        });

        addProperties(props);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot == null || !slot.hasStack())
            return newStack;

        int invSize = MinerPropertyDelegate.SIZE;
        ItemStack originalStack = slot.getStack();
        newStack = originalStack.copy();

        SimpleStationsMiner.LOGGER.info("SLOT: " + invSlot);
        if (invSlot < invSize) {
            SimpleStationsMiner.LOGGER.info("1");
            if (!this.insertItem(originalStack, invSize, this.slots.size(), true)) {
                SimpleStationsMiner.LOGGER.info("2");
                return ItemStack.EMPTY;
            }
        } else if (!this.insertItem(originalStack, 0, invSize, false)) {
            SimpleStationsMiner.LOGGER.info("3");
            return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        SimpleStationsMiner.LOGGER.info("4");
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int k = 0; k < 3; ++k)
            for (int j = 0; j < 9; ++j)
                addSlot(new Slot(inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int j = 0; j < 9; ++j)
            addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }
}

package com.ave.smartminer.screen;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerContainer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SmartMinerMenu extends AbstractContainerMenu {
    public final Level level;
    private final SmartMinerContainer blockEntity;

    public SmartMinerMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory,
                (SmartMinerContainer) inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public SmartMinerMenu(int containerId, Inventory inventory, SmartMinerContainer blockEntity) {
        super(ModMenuTypes.SMART_MINER_MENU.get(), containerId);
        this.level = inventory.player.level();
        this.blockEntity = blockEntity;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 8, 52));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 80, 37));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 152, 52));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player,
                SmartMiner.IRON_MINER_BLOCK.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int k = 0; k < 3; ++k)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));

    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int j = 0; j < 9; ++j)
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }
}

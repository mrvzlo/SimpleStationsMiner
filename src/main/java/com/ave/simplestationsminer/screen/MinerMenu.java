package com.ave.simplestationsminer.screen;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.mainblock.StationContainer;
import com.ave.simplestationscore.screen.BaseStationMenu;
import com.ave.simplestationscore.screen.DataSlotHelper;
import com.ave.simplestationsminer.Registrations;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.UpgradeType;
import com.ave.simplestationsminer.uihelpers.UIBlocks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class MinerMenu extends BaseStationMenu {

    public MinerMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        super(containerId, inventory, data, Registrations.MINER_MENU.get());
    }

    public MinerMenu(int containerId, Inventory inventory, StationContainer be) {
        super(containerId, inventory, be, Registrations.MINER_MENU.get());
    }

    public void addItemSlots() {
        addItemSlot(blockEntity.inventory, MinerBlockEntity.OUTPUT_SLOT, UIBlocks.OUT_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.FUEL_SLOT, UIBlocks.FUEL_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.TYPE_SLOT, UIBlocks.FILTER_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.PORTAL_SLOT, UIBlocks.PORTAL_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.DRILL_SLOT, UIBlocks.DRILL_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.COOLANT_SLOT, UIBlocks.COOL_SLOT);
        addItemSlot(blockEntity.inventory, MinerBlockEntity.REDSTONE_SLOT, UIBlocks.CATA_SLOT);
    }

    @Override
    public void addDataSlots(BaseStationBlockEntity station) {
        var miner = (MinerBlockEntity) station;
        super.addDataSlots(station);
        var coolant = miner.resources.get(MinerBlockEntity.COOLANT_SLOT);
        var catalyst = miner.resources.get(MinerBlockEntity.REDSTONE_SLOT);
        addDataSlot(DataSlotHelper.fromInt(() -> coolant.get(), v -> coolant.set(v)));
        addDataSlot(DataSlotHelper.fromInt(() -> catalyst.get(), v -> catalyst.set(v)));
        addDataSlot(DataSlotHelper.fromBool(() -> miner.hasNetherUpgrade, v -> miner.hasNetherUpgrade = v));
        addDataSlot(DataSlotHelper.fromInt(() -> miner.drill.ordinal(), v -> miner.drill = UpgradeType.values()[v]));
        addDataSlot(DataSlotHelper.fromInt(() -> miner.drillCount, v -> miner.drillCount = v));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player,
                Registrations.MINER.getBlock());
    }
}

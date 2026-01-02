package com.ave.simplestationsminer.blockentity;

import java.util.ArrayList;
import java.util.List;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.resources.EnergyResource;
import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.Registrations;
import com.ave.simplestationsminer.blockentity.helpers.MinerFluidItemResource;
import com.ave.simplestationsminer.blockentity.helpers.MinerItemHandler;
import com.ave.simplestationsminer.blockentity.managers.OreHashManager;
import com.ave.simplestationsminer.blockentity.managers.UpgradeManager;
import com.ave.simplestationsminer.blockentity.managers.WorkManager;
import com.ave.simplestationsminer.screen.MinerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class MinerBlockEntity extends BaseStationBlockEntity {
    public static final int TYPE_SLOT = 2;
    public static final int COOLANT_SLOT = 3;
    public static final int REDSTONE_SLOT = 4;
    public static final int PORTAL_SLOT = 5;
    public static final int DRILL_SLOT = 6;

    public boolean invalidDepth = false;

    public List<UpgradeType> upgrades = new ArrayList<UpgradeType>();
    public boolean hasNetherUpgrade = false;
    public UpgradeType drill = UpgradeType.Unknown;
    public int drillCount = 0;

    public int upgradesHash = 0;

    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(Registrations.MINER.getEntity(), pos, state);

        if (pos != null)
            invalidDepth = pos.getY() > Config.MAX_Y.get();

        inventory = new MinerItemHandler(7) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };

        resources.put(FUEL_SLOT, new EnergyResource(Config.FUEL_CAPACITY.get(), 16, Config.FUEL_PER_COAL.get()));
        var coolRes = new MinerFluidItemResource(Config.MAX_COOLANT.get(), Config.COOLANT_USAGE.get(), "coolant");
        resources.put(COOLANT_SLOT, coolRes);
        var cataRes = new MinerFluidItemResource(Config.MAX_CATALYST.get(), Config.CATALYST_USAGE.get(), "catalyst");
        resources.put(REDSTONE_SLOT, cataRes);
    }

    @Override()
    public void tick() {
        if (invalidDepth)
            return;

        if (!level.isClientSide) {
            var shouldUpdate = UpgradeManager.checkUpgradeSlots(this);
            if (shouldUpdate)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
        super.tick();
    }

    @Override
    protected void preWorkTick() {
        speed = WorkManager.getSpeedValue(this);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        upgrades.clear();
        for (byte up : tag.getByteArray("upgrades"))
            upgrades.add(UpgradeType.values()[up]);
        UpgradeManager.applyUpgrades(this);
    }

    @Override
    protected void saveAll(CompoundTag tag) {
        super.saveAll(tag);
        tag.putByteArray("upgrades", upgrades.stream().map(x -> (byte) x.ordinal()).toList());
    }

    public SoundEvent getWorkSound() {
        return SoundEvents.DEEPSLATE_BREAK;
    }

    @Override
    public MinerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MinerMenu(containerId, inventory, this);
    }

    @Override
    public int getMaxProgress() {
        return Config.MAX_PROGRESS.getAsInt();
    }

    @Override
    public ItemStack getProduct(boolean __) {
        if (!isValidWorld())
            return ItemStack.EMPTY;
        var stack = OreHashManager.getItemStack(type);
        if (drill.ordinal() < UpgradeManager.getMinDrill(stack.getItem()).ordinal())
            return ItemStack.EMPTY;
        return stack;
    }

    @Override
    protected int getCurrentType() {
        return OreHashManager.getHash(inventory.getStackInSlot(TYPE_SLOT));
    }

    public boolean isValidWorld() {
        return hasNetherUpgrade || OreHashManager.isRegular(type);
    }

}

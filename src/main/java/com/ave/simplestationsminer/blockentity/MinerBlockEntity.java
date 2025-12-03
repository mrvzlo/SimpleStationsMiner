package com.ave.simplestationsminer.blockentity;

import java.util.ArrayList;
import java.util.List;
import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.blockentity.handlers.InputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.OutputItemHandler;

import com.ave.simplestationsminer.blockentity.managers.ResourceManager;
import com.ave.simplestationsminer.blockentity.managers.UpgradeManager;
import com.ave.simplestationsminer.blockentity.managers.WorkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

public class MinerBlockEntity extends ModContainer {
    public EnergyStorage fuel;
    public int fuelHigh = 0;
    public int fuelLow = 0;
    public Item type = null;
    public float progress = 0;
    public int coolant = 0;
    public int redstone = 0;
    public boolean working = false;
    public boolean invalidDepth = false;

    public List<UpgradeType> upgrades = new ArrayList<UpgradeType>();
    public boolean hasNetherUpdate = false;
    public Item drill = null;
    public int drillCount = 0;

    public float speed = 1;
    public int outputSize = 1;
    public int powerConsuption = 0;

    public int upgradesHash = 0;
    public int soundCooldown = 0;

    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 7);
        if (pos != null)
            invalidDepth = pos.getY() > Config.MAX_Y.get();
        fuel = new EnergyStorage(Config.FUEL_CAPACITY.get());
    }

    public void tick() {
        if (level.isClientSide || invalidDepth)
            return;

        if (progress >= Config.MAX_PROGRESS.get())
            progress -= Config.MAX_PROGRESS.get();

        var shouldUpdate = checkNewType() || UpgradeManager.checkUpgradeSlots(this);
        ResourceManager.checkAllResources(this);
        working = WorkManager.getWorking(this);

        if (shouldUpdate)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);

        fuelHigh = fuel.getEnergyStored() >> 16;
        fuelLow = fuel.getEnergyStored() & 0xFFFF;
        if (type == null || !working)
            return;

        WorkManager.performWorkTick(this);

        if (progress < Config.MAX_PROGRESS.get())
            return;

        WorkManager.performWorkEnd(this);
        setChanged();
    }

    private boolean checkNewType() {
        Item newType = getCurrentFilter();
        if (type == null && newType == null || type != null && type.equals(newType))
            return false;

        type = newType;
        progress = 0;
        speed = WorkManager.getSpeedValue(this);
        outputSize = WorkManager.getOutputSize(type);
        return true;
    }

    public boolean isValidWorld() {
        if (this.type == null || hasNetherUpdate)
            return true;
        return !this.type.toString().contains("nether");
    }

    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this.fuel);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new OutputItemHandler(inventory));
    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new InputItemHandler(inventory));

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY)
            return energy.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return side.equals(Direction.DOWN) ? outputHandler.cast() : inputHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveAll(tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        saveAll(tag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        type = getCurrentFilter();
        fuel = new EnergyStorage(Config.FUEL_CAPACITY.get(), Config.FUEL_CAPACITY.get(), Config.FUEL_CAPACITY.get(),
                tag.getInt("fuel"));
        progress = tag.getFloat("progress");
        coolant = tag.getInt("coolant");
        redstone = tag.getInt("redstone");
        upgrades.clear();
        outputSize = WorkManager.getOutputSize(type);
        for (byte up : tag.getByteArray("upgrades"))
            upgrades.add(UpgradeType.values()[up]);
        UpgradeManager.applyUpgrades(this);
        speed = WorkManager.getSpeedValue(this);
    }

    private void saveAll(CompoundTag tag) {
        tag.putInt("fuel", fuel.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
        tag.putByteArray("upgrades", upgrades.stream().map(x -> (byte) x.ordinal()).toList());
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.getStackInSlot(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

}

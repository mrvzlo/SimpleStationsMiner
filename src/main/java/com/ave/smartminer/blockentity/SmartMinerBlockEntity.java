package com.ave.smartminer.blockentity;

import com.ave.smartminer.sound.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    public static final int ENERGY_PER_TICK = 100;
    public static final int MAX_PROGRESS = 400;
    public static final int MAX_COOLANT = 20;
    public static final int MAX_REDSTONE = 20;
    private static final int FUEL_PER_COAL = MAX_PROGRESS * 3 * ENERGY_PER_TICK;
    public static final int FUEL_CAPACITY = FUEL_PER_COAL * 10;
    private static final int INCREMENT = 1;
    public EnergyStorage fuel = new EnergyStorage(FUEL_CAPACITY);
    public Item type = null;
    public int progress = 0;
    public int coolant = 0;
    public int redstone = 0;
    public boolean working = false;
    public boolean invalidDepth = false;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 5);
        if (pos != null)
            invalidDepth = pos.getY() > 20;
    }

    public void tick() {
        if (level.isClientSide || invalidDepth)
            return;

        if (progress > MAX_PROGRESS)
            progress -= MAX_PROGRESS;

        checkNewType();
        checkResource(FUEL_SLOT, Items.COAL_BLOCK, FUEL_PER_COAL, FUEL_CAPACITY, ResourceType.FUEL);
        checkResource(REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, MAX_REDSTONE, ResourceType.REDSTONE);
        checkResource(COOLANT_SLOT, Items.LAPIS_BLOCK, 1, MAX_COOLANT, ResourceType.COOLANT);
        if (type == null)
            return;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = fuel.getEnergyStored() >= ENERGY_PER_TICK && coolant > 0 && redstone > 0
                && slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type);

        if (!working)
            return;

        progress++;
        fuel.extractEnergy(ENERGY_PER_TICK, false);
        playSound();

        if (progress < MAX_PROGRESS)
            return;

        coolant--;
        redstone--;
        ItemStack toAdd = new ItemStack(type);
        toAdd.setCount(slot.getCount() + (isCheap() ? INCREMENT + 1 : INCREMENT));
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);
        setChanged();
    }

    private boolean checkResource(int slot, Item blockItem, int singleValue, int maxCapacity, ResourceType type) {
        ItemStack stack = inventory.getStackInSlot(slot);
        int increment = stack.getItem().equals(blockItem) ? singleValue * 9 : singleValue;

        if (stack.isEmpty() || getResourceValue(type) + increment > maxCapacity)
            return false;

        stack.shrink(1);
        inventory.setStackInSlot(slot, stack);
        addResource(type, increment);
        return true;
    }

    private void addResource(ResourceType type, int amount) {
        switch (type) {
            case FUEL -> fuel.receiveEnergy(amount, false);
            case COOLANT -> coolant += amount;
            case REDSTONE -> redstone += amount;
        }
    }

    public int soundCooldown = 0;

    private void playSound() {
        if (soundCooldown > 0) {
            soundCooldown--;
            return;
        }
        soundCooldown += 25;
        level.playSound(null, getBlockPos(), ModSounds.WORK_SOUND.get(), SoundSource.BLOCKS);
    }

    private int getResourceValue(ResourceType type) {
        return switch (type) {
            case FUEL -> fuel.getEnergyStored();
            case COOLANT -> coolant;
            case REDSTONE -> redstone;
        };
    }

    private boolean isCheap() {
        return type.equals(Items.SAND) || type.equals(Items.STONE) || type.equals(Items.GRAVEL)
                || type.equals(Items.IRON_ORE);
    }

    private boolean checkNewType() {
        Item newType = getCurrentFilter();
        if (type == null && newType == null || type != null && type.equals(newType))
            return false;

        type = newType;
        progress = 0;
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fuel", fuel.getEnergyStored());
        tag.putInt("progress", progress);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        type = getCurrentFilter();
        fuel = new EnergyStorage(FUEL_CAPACITY, FUEL_CAPACITY, FUEL_CAPACITY, tag.getInt("fuel"));
        progress = tag.getInt("progress");
        coolant = tag.getInt("coolant");
        redstone = tag.getInt("redstone");
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.getStackInSlot(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}

package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.blockentity.handlers.InputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.OutputItemHandler;
import com.ave.simplestationsminer.sound.ModSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

    private float speed = 1;
    private int outputSize = 1;

    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 5);
        if (pos != null)
            invalidDepth = pos.getY() > Config.MAX_Y.get();
        fuel = new EnergyStorage(Config.FUEL_CAPACITY.get());
    }

    public void tick() {
        if (level.isClientSide || invalidDepth)
            return;

        if (progress >= Config.MAX_PROGRESS.get())
            progress -= Config.MAX_PROGRESS.get();

        checkNewType();
        checkResource(FUEL_SLOT, Items.COAL_BLOCK, Config.FUEL_PER_COAL.get(), Config.FUEL_CAPACITY.get(),
                ResourceType.FUEL);

        if (Config.isExtendedMod()) {
            checkResource(REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, Config.MAX_CATALYST.get(), ResourceType.REDSTONE);
            checkResource(COOLANT_SLOT, Items.LAPIS_BLOCK, 1, Config.MAX_COOLANT.get(), ResourceType.COOLANT);
        }
        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = getWorking(slot);

        fuelHigh = fuel.getEnergyStored() >> 16;
        fuelLow = fuel.getEnergyStored() & 0xFFFF;
        if (type == null || !working)
            return;

        progress += speed;
        fuel.extractEnergy(Config.ENERGY_PER_TICK.get(), false);
        playSound();

        if (progress < Config.MAX_PROGRESS.get())
            return;

        if (Config.isExtendedMod()) {
            coolant--;
            redstone--;
        }
        ItemStack toAdd = new ItemStack(type);
        toAdd.setCount(slot.getCount() + outputSize);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);
        setChanged();
    }

    private boolean getWorking(ItemStack slot) {
        if (type == null)
            return false;
        if (Config.isExtendedMod() && (coolant < 1 || redstone < 1))
            return false;
        if (fuel.getEnergyStored() < Config.ENERGY_PER_TICK.get())
            return false;
        if (slot.getCount() == 0)
            return true;
        if (slot.getCount() + outputSize > slot.getMaxStackSize())
            return false;
        return slot.getItem().equals(type);
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

    private int getOutputSize() {
        return getOutputSize(type);
    }

    public static int getOutputSize(Item item) {
        if (item == null)
            return 1;

        if (item.equals(Items.SAND) || item.equals(Items.STONE) || item.equals(Items.GRAVEL))
            return 8;

        if (item.equals(Items.COAL_ORE) || item.equals(Items.DEEPSLATE_COAL_ORE)
                || item.equals(Items.COPPER_ORE) || item.equals(Items.DEEPSLATE_COPPER_ORE)
                || item.equals(Items.NETHER_QUARTZ_ORE))
            return 2;

        return 1;
    }

    private int getSpeedMod() {
        return getSpeedMod(type);
    }

    public static int getSpeedMod(Item item) {
        if (item == null)
            return 1;
        ItemStack stack = new ItemStack(item);
        if (stack.is(ItemTags.DIAMOND_ORES))
            return 5;
        if (stack.is(ItemTags.EMERALD_ORES))
            return 6;
        if (item.equals(Items.ANCIENT_DEBRIS))
            return 20;

        return 1;
    }

    private void checkNewType() {
        Item newType = getCurrentFilter();
        if (type == null && newType == null || type != null && type.equals(newType))
            return;

        type = newType;
        progress = 0;
        speed = 1f / getSpeedMod();
        outputSize = getOutputSize();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
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
        speed = 1f / getSpeedMod();
        outputSize = getOutputSize();
    }

    private void saveAll(CompoundTag tag) {
        tag.putInt("fuel", fuel.getEnergyStored());
        tag.putFloat("progress", progress);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.getStackInSlot(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}

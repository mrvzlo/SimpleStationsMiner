package com.ave.simplestationsminer.blockentity;

import org.jetbrains.annotations.Nullable;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.blockentity.handlers.MinerSidedInventory;
import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.screen.MinerScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class MinerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, MinerSidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);

    public Item type = null;
    public int fuel = 0;
    public float progress = 0;
    public int coolant = 0;
    public int redstone = 0;
    public boolean working = false;
    public boolean invalidDepth = false;

    private float speed = 1;
    private int outputSize = 1;

    public static final int OUTPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int TYPE_SLOT = 2;
    public static final int COOLANT_SLOT = 3;
    public static final int REDSTONE_SLOT = 4;
    public final MinerPropertyDelegate props;

    public MinerBlockEntity(BlockPos pos, BlockState state) {
        super(Registrations.MINER_BLOCK_ENTITY, pos, state);
        if (pos != null)
            invalidDepth = pos.getY() > Config.MAX_HEIGHT;
        this.props = MinerPropertyDelegate.create(this);
    }

    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.simplestationsminer.miner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MinerScreenHandler(syncId, playerInventory, this, this.props);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    public void tick() {
        if (world.isClient || invalidDepth)
            return;

        if (progress >= Config.MAX_PROGRESS)
            progress -= Config.MAX_PROGRESS;

        checkNewType();
        checkResource(FUEL_SLOT, Items.COAL_BLOCK, Config.FUEL_PER_COAL,
                Config.FUEL_CAPACITY, ResourceType.FUEL);
        checkResource(REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, Config.MAX_CATALYST,
                ResourceType.REDSTONE);
        checkResource(COOLANT_SLOT, Items.LAPIS_BLOCK, 1, Config.MAX_COOLANT,
                ResourceType.COOLANT);
        ItemStack slot = inventory.get(OUTPUT_SLOT);
        working = getWorking(slot);

        if (type == null || !working)
            return;

        progress += speed;
        fuel -= Config.ENERGY_PER_TICK;
        playSound();

        if (progress < Config.MAX_PROGRESS)
            return;

        coolant--;
        redstone--;

        ItemStack toAdd = new ItemStack(type);
        toAdd.setCount(slot.getCount() + outputSize);
        inventory.set(OUTPUT_SLOT, toAdd);
    }

    private boolean getWorking(ItemStack slot) {
        if (type == null)
            return false;
        if (coolant < 1 || redstone < 1)
            return false;
        if (fuel < Config.ENERGY_PER_TICK)
            return false;
        if (slot.getCount() == 0)
            return true;
        if (slot.getCount() + outputSize > slot.getMaxCount())
            return false;
        return slot.getItem().equals(type);
    }

    private boolean checkResource(int slot, Item blockItem, int singleValue, int maxCapacity, ResourceType type) {
        ItemStack stack = inventory.get(slot);
        int increment = stack.getItem().equals(blockItem) ? singleValue * 9 : singleValue;

        if (stack.isEmpty() || getResourceValue(type) + increment > maxCapacity)
            return false;

        stack.decrement(1);
        inventory.set(slot, stack);
        addResource(type, increment);
        return true;
    }

    private void addResource(ResourceType type, int amount) {
        switch (type) {
            case FUEL -> fuel += amount;
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
        world.playSound(null, getPos(), Registrations.WORK_SOUND, SoundCategory.BLOCKS);
    }

    private int getResourceValue(ResourceType type) {
        return switch (type) {
            case FUEL -> fuel;
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
        if (stack.isIn(ItemTags.DIAMOND_ORES))
            return 5;
        if (stack.isIn(ItemTags.EMERALD_ORES))
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
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        type = getCurrentFilter();
        fuel = nbt.getInt("fuel");
        progress = nbt.getFloat("progress");
        coolant = nbt.getInt("coolant");
        redstone = nbt.getInt("redstone");
        speed = 1f / getSpeedMod();
        outputSize = getOutputSize();
        invalidDepth = pos.getY() > Config.MAX_HEIGHT;
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("fuel", fuel);
        nbt.putFloat("progress", progress);
        nbt.putInt("coolant", coolant);
        nbt.putInt("redstone", redstone);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.get(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}

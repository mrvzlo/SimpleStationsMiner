package com.ave.smartminer.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    private int progress = 0;
    private static final int MAX_PROGRESS = 10;
    private static final int INCREMENT = 1;
    public SmartMinerType type = SmartMinerType.Unknown;
    public boolean working = false;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    public void tick() {
        if (level.isClientSide)
            return;

        checkNewType();
        if (type == null || type == SmartMinerType.Unknown)
            return;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type.minedItem);
        if (!working)
            return;

        progress++;
        if (progress < MAX_PROGRESS)
            return;

        progress = 0;
        ItemStack toAdd = new ItemStack(type.minedItem);
        toAdd.setCount(slot.getCount() + INCREMENT);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);

        setChanged();
    }

    private void checkNewType() {
        Item slot = inventory.getStackInSlot(2).getItem();
        SmartMinerType newType = SmartMinerType.findMatch(slot);
        if (type == newType)
            return;

        type = newType;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        progress = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("type", type.getSerializedName());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        String typeStr = tag.getString("type");
        type = SmartMinerType.findMatch(typeStr);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

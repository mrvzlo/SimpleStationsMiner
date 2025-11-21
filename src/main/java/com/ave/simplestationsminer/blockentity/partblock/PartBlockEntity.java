package com.ave.simplestationsminer.blockentity.partblock;

import org.jetbrains.annotations.Nullable;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.handlers.MinerSidedInventory;
import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.screen.MinerScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class PartBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, MinerSidedInventory {

    private BlockPos controllerPos;

    public PartBlockEntity(BlockPos pos, BlockState state) {
        super(Registrations.PART_BLOCK_ENTITY, pos, state);
    }

    public void setControllerPos(BlockPos pos) {
        controllerPos = pos;
        if (world == null || world.isClient)
            return;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public DefaultedList<ItemStack> getItems() {
        MinerBlockEntity controller = (MinerBlockEntity) world.getBlockEntity(controllerPos);
        return controller.getItems();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.simplestationsminer.miner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MinerScreenHandler(syncId, playerInventory, this.controllerPos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.controllerPos;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        controllerPos = BlockPos.fromLong(nbt.getLong("controller"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putLong("controller", controllerPos.asLong());
        super.writeNbt(nbt, registryLookup);
    }
}

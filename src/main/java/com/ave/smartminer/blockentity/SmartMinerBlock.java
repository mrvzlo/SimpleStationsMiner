package com.ave.smartminer.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SmartMinerBlock extends Block implements EntityBlock {
    public static final BooleanProperty WORKING = BooleanProperty.create("working");
    public static final EnumProperty<SmartMinerType> TYPE = EnumProperty.create("type", SmartMinerType.class);

    public SmartMinerBlock(Properties props, SmartMinerType type) {
        super(props);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(WORKING, false).setValue(TYPE, type));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        SmartMinerBlockEntity miner = new SmartMinerBlockEntity(pos, state);
        miner.type = state.getValue(TYPE);
        return miner;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof SmartMinerBlockEntity miner)
                miner.tick();
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WORKING);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stackm, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        SmartMinerBlockEntity blockEntity = (SmartMinerBlockEntity) level.getBlockEntity(pos);
        player.openMenu(new SimpleMenuProvider(blockEntity, Component.literal("SmartMiner")), pos);
        return ItemInteractionResult.SUCCESS;
    }
}

package com.ave.smartminer;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class AutoMinerBlock extends Block implements EntityBlock {
    public static final BooleanProperty WORKING = BooleanProperty.create("working");
    public static final EnumProperty<AutoMinerType> TYPE = EnumProperty.create("type", AutoMinerType.class);

    public AutoMinerBlock(Properties props, AutoMinerType type) {
        super(props);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(WORKING, false).setValue(TYPE, type));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        AutoMinerBlockEntity miner = new AutoMinerBlockEntity(pos, state);
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
            if (be instanceof AutoMinerBlockEntity miner)
                miner.tick();
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WORKING);
    }
}

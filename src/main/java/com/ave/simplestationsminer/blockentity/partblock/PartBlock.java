package com.ave.simplestationsminer.blockentity.partblock;

import org.jetbrains.annotations.Nullable;

import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PartBlock extends BlockWithEntity {
    public PartBlock(Settings props) {
        super(props);
    }

    @Override
    protected MapCodec<BlockWithEntity> getCodec() {
        return createCodec(PartBlock::new);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PartBlockEntity(pos, state);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return state;
        super.onBreak(world, pos, state, player);

        BlockPos controllerPos = part.getControllerPos();
        BlockState controllerState = world.getBlockState(controllerPos);
        if (controllerState.getBlock() instanceof MinerBlock miner)
            miner.onBreak(world, pos, state, player);
        return state;
    }
}

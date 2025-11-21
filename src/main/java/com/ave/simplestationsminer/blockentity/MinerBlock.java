package com.ave.simplestationsminer.blockentity;

import org.jetbrains.annotations.Nullable;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.partblock.PartBlockEntity;
import com.ave.simplestationsminer.registrations.Registrations;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class MinerBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing");

    public MinerBlock(Settings s) {
        super(s);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<BlockWithEntity> getCodec() {
        return createCodec(MinerBlock::new);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MinerBlockEntity(pos, state);
    }

    // @Override
    // public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    // return this.defaultBlockState().setValue(FACING,
    // ctx.getHorizontalDirection().getOpposite());
    // }

    // @Override
    // protected void createBlockStateDefinition(StateDefinition.Builder<Block,
    // BlockState> builder) {
    // builder.add(FACING);
    // }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof MinerBlockEntity miner)
                miner.tick();
        };
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(Direction.NORTH, dz).offset(Direction.EAST, dx);
                if (p.equals(pos) || world.getBlockState(p).isAir())
                    continue;
                SimpleStationsMiner.LOGGER.info("NO");
                return false;
            }

        SimpleStationsMiner.LOGGER.info("YES");
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient)
            return;

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(Direction.NORTH, dz).offset(Direction.EAST, dx);

                if (p.equals(pos))
                    continue;

                BlockState block = Registrations.MINER_PART.getDefaultState();
                world.setBlockState(p, block, 3);

                PartBlockEntity be = (PartBlockEntity) world.getBlockEntity(p);
                be.setControllerPos(pos);
            }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.isClient)
            return state;

        BlockEntity controller = world.getBlockEntity(pos);
        if (controller instanceof MinerBlockEntity miner) {
            // Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Registrations.MINER_BLOCK, 1));
            // Containers.dropContents(world, pos, miner.inventory.getAsList());
        }

        super.onBreak(world, pos, state, player);

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(Direction.NORTH, dz).offset(Direction.EAST, dx);

                if (p.equals(pos))
                    continue;

                BlockEntity be = world.getBlockEntity(p);
                if (be instanceof PartBlockEntity)
                    world.removeBlock(p, false);
            }
        return state;
    }
}

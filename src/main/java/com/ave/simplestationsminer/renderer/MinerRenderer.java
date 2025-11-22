package com.ave.simplestationsminer.renderer;

import java.util.Optional;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.registrations.Registrations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MinerRenderer implements BlockEntityRenderer<MinerBlockEntity> {

    public MinerRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(MinerBlockEntity be, float pt, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        if (be.type == null)
            return;

        ItemStack stack = new ItemStack(be.type);
        Direction direction = getDirection(be);
        World world = be.getWorld();
        long gameTime = world.getTime() % 1000;

        drawBlock(m, itemRenderer, stack, light, vertexConsumers, getZShift(gameTime, 0), 0.5f, direction, world);
        drawBlock(m, itemRenderer, stack, light, vertexConsumers, getZShift(gameTime, 250), 0.4f, direction, world);
        drawBlock(m, itemRenderer, stack, light, vertexConsumers, getZShift(gameTime, 500), 0.5f, direction, world);
        drawBlock(m, itemRenderer, stack, light, vertexConsumers, getZShift(gameTime, 750), 0.6f, direction, world);
    }

    private Direction getDirection(MinerBlockEntity be) {
        BlockState state = be.getCachedState();
        if (state == null)
            return Direction.NORTH;
        if (!state.isOf(Registrations.MINER_BLOCK))
            return Direction.NORTH;
        Optional<Direction> dir = state.getOrEmpty(MinerBlock.FACING);
        if (dir.isEmpty())
            return Direction.NORTH;
        return dir.get();
    }

    private float getZShift(long gameTime, int delay) {
        float shift = ((gameTime + delay) % 1000) / 200f - 0.5f;
        return Math.clamp(shift, -0.5f, 1.5f);
    }

    private void drawBlock(MatrixStack m, ItemRenderer itemRenderer, ItemStack stack,
            int light, VertexConsumerProvider v, float sx, float sz, Direction direction, World world) {
        if (sx >= 1.5f || sx <= -0.5f)
            return;
        if (direction == Direction.WEST || direction == Direction.SOUTH)
            sx = 1 - sx;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            float temp = sx;
            sx = sz;
            sz = temp;
        }

        m.push();
        m.translate(sx, 0.5f, sz);
        m.scale(0.7f, 0.7f, 0.7f);

        itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, m, v, world, 0);
        m.pop();
    }
}

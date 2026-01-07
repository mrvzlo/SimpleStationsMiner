package com.ave.simplestationsminer.renderer;

import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.UpgradeType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class MinerRenderer implements BlockEntityRenderer<MinerBlockEntity> {

    public MinerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MinerBlockEntity be, float pt, PoseStack pose, MultiBufferSource buf, int light,
            int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Direction direction = be.getBlockState().getValue(MinerBlock.FACING);
        long gameTime = be.getLevel().getGameTime();
        float rotation = be.type == -1 ? 0 : ((gameTime * 4) % 360);
        if (be.drill != UpgradeType.Unknown) {
            var drill = new ItemStack(be.drill.item);
            if (be.drillCount > 0)
                drawBlock(pose, itemRenderer, drill, be, buf, -0.5f, -0.5f, direction, 1, rotation);
            if (be.drillCount > 1)
                drawBlock(pose, itemRenderer, drill, be, buf, -0.5f, 1.5f, direction, 1, rotation);
        }

        if (be.type == -1)
            return;

        var stack = be.getProduct(false);

        int blockIndex = (int) ((gameTime / 200) % 4);
        float[] yScales = { 0.5f, 0.4f, 0.5f, 0.6f };

        drawBlock(pose, itemRenderer, stack, be, buf, getZShift(gameTime), yScales[blockIndex],
                direction, 0.7f, 0);

    }

    private float getZShift(long gameTime) {
        float shift = (gameTime % 200) / 100f - 0.5f;
        return Math.min(Math.max(shift, -0.5f), 1.5f);
    }

    private void drawBlock(PoseStack pose, ItemRenderer itemRenderer, ItemStack stack,
            MinerBlockEntity be, MultiBufferSource buf, float sx, float sz, Direction direction, float size,
            float rotate) {
        if (sx >= 1.5f || sx < -0.5f)
            return;
        if (direction == Direction.WEST || direction == Direction.SOUTH)
            sx = 1 - sx;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            float temp = sx;
            sx = sz;
            sz = temp;
        }

        pose.pushPose();
        pose.translate(sx, 0.5f, sz);
        pose.scale(0.7f, size, 0.7f);
        if (rotate > 0)
            pose.mulPose(Axis.YP.rotationDegrees(rotate));

        int light = getLightLevel(be.getLevel(), be.getBlockPos());
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, pose, buf,
                be.getLevel(), 1);
        pose.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}

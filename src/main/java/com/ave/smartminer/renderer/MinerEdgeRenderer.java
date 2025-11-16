package com.ave.smartminer.renderer;

import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerType;
import com.ave.smartminer.blockentity.partblock.PartBlock;
import com.ave.smartminer.blockentity.partblock.PartBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class MinerEdgeRenderer implements BlockEntityRenderer<PartBlockEntity> {

    public MinerEdgeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PartBlockEntity be, float pt, PoseStack pose, MultiBufferSource buf, int light,
            int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockPos controllerPos = be.getControllerPos();
        boolean isEdge = be.getBlockState().getValue(PartBlock.EDGE);
        if (controllerPos == null || !isEdge)
            return;
        SmartMinerBlockEntity controller = (SmartMinerBlockEntity) be.getLevel().getBlockEntity(controllerPos);
        if (controller == null || controller.type == null || controller.type == SmartMinerType.Unknown)
            return;

        ItemStack stack = new ItemStack(controller.type.minedItem);
        drawBlock(pose, itemRenderer, stack, be, buf);
    }

    private void drawBlock(PoseStack pose, ItemRenderer itemRenderer, ItemStack stack,
            PartBlockEntity be, MultiBufferSource buf) {
        pose.pushPose();
        pose.translate(0.5f, 0.5f, 0.5f);
        pose.scale(0.7f, 0.7f, 0.7f);

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

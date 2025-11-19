package com.ave.smartminer.screen;

import java.util.Arrays;
import java.util.List;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmartMinerScreen extends AbstractContainerScreen<SmartMinerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID,
            "textures/gui/base_miner_gui.png");

    public SmartMinerScreen(SmartMinerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        super.render(gfx, mouseX, mouseY, partialTicks);
        this.renderTooltip(gfx, mouseX, mouseY);

        if (!(menu.blockEntity instanceof SmartMinerBlockEntity miner))
            return;

        String fuelPart = miner.fuel.getEnergyStored() + " / " + SmartMinerBlockEntity.FUEL_CAPACITY;
        List<Component> fuelText = Arrays.asList(Component.translatable("screen.smartminer.fuel"),
                Component.literal(fuelPart));
        this.renderTooltip(gfx, mouseX, mouseY, 7, 14, 18, 37, fuelText);

        String coolantPart = miner.coolant + " / " + SmartMinerBlockEntity.MAX_COOLANT;
        List<Component> coolantText = Arrays.asList(Component.translatable("screen.smartminer.coolant"),
                Component.literal(coolantPart));
        this.renderTooltip(gfx, mouseX, mouseY, 25, 14, 18, 37, coolantText);

        String redstonePart = miner.redstone + " / " + SmartMinerBlockEntity.MAX_REDSTONE;
        List<Component> redstoneText = Arrays.asList(Component.translatable("screen.smartminer.catalysis"),
                Component.literal(redstonePart));
        this.renderTooltip(gfx, mouseX, mouseY, 43, 14, 18, 37, redstoneText);

        if (miner.progress > 0) {
            int progressPart = 100 * miner.progress / miner.MAX_PROGRESS;
            List<Component> progressText = Arrays.asList(Component.literal(progressPart + "%"));
            this.renderTooltip(gfx, mouseX, mouseY, 79, 54, 18, 5, progressText);
        }

        if (miner.type == null) {
            List<Component> filterText = Arrays.asList(Component.translatable("screen.smartminer.filter"));
            this.renderTooltip(gfx, mouseX, mouseY, 151, 51, 18, 18, filterText);
        }

        if (miner.invalidDepth) {
            List<Component> filterText = Arrays.asList(Component.translatable("screen.smartminer.depthError"));
            this.renderTooltip(gfx, mouseX, mouseY, 133, 7, 36, 8, filterText);
        }
    }

    private void renderTooltip(GuiGraphics gfx, int mouseX, int mouseY, int left, int top, int w, int h,
            List<Component> text) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        left += x;
        top += y;
        int right = left + w;
        int bottom = h + top;

        if (mouseX < left || mouseX >= right || mouseY < top || mouseY >= bottom)
            return;

        gfx.renderComponentTooltip(font, text, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float tick, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

        if (!(menu.blockEntity instanceof SmartMinerBlockEntity miner))
            return;

        int tickAlpha = 96 + (int) (63 * Math.sin(System.currentTimeMillis() / 400.0));
        int borderColor = (tickAlpha << 24) | 0xFF0000;
        int progressPart = miner.progress * 16 / SmartMinerBlockEntity.MAX_PROGRESS;
        graphics.fill(x + 80, y + 55, x + 80 + progressPart, y + 58, 0xFFCCFEDD);

        if (miner.invalidDepth) {
            graphics.drawString(font, "Y > 20", x + 138, y + 7, borderColor, false);
            return;
        }

        int fuelPart = miner.fuel.getEnergyStored() * 35 / SmartMinerBlockEntity.FUEL_CAPACITY;
        graphics.fill(x + 8, y + 50 - fuelPart, x + 24, y + 50, 0xAA225522);
        int coolantPart = miner.coolant * 35 / SmartMinerBlockEntity.MAX_COOLANT;
        graphics.fill(x + 26, y + 50 - coolantPart, x + 42, y + 50, 0xAA3333AA);
        int redstonePart = miner.redstone * 35 / SmartMinerBlockEntity.MAX_REDSTONE;
        graphics.fill(x + 44, y + 50 - redstonePart, x + 60, y + 50, 0xAABB2211);

        if (fuelPart == 0)
            drawBorder(graphics, x + 8, y + 52, borderColor);
        if (coolantPart == 0)
            drawBorder(graphics, x + 26, y + 52, borderColor);
        if (redstonePart == 0)
            drawBorder(graphics, x + 44, y + 52, borderColor);
    }

    private void drawBorder(GuiGraphics g, int x, int y, int color) {
        g.fill(x, y, x + 16, y + 1, color);
        g.fill(x, y + 15, x + 16, y + 16, color);
        g.fill(x, y, x + 1, y + 16, color);
        g.fill(x + 15, y, x + 16, y + 16, color);
    }
}

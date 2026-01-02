package com.ave.simplestationsminer.screen;

import java.util.Arrays;
import java.util.List;

import com.ave.simplestationscore.mainblock.BaseStationBlockEntity;
import com.ave.simplestationscore.screen.BaseStationMenu;
import com.ave.simplestationscore.screen.BaseStationScreen;
import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.uihelpers.UIBlocks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MinerScreen extends BaseStationScreen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SimpleStationsMiner.MODID,
            "textures/gui/base_miner_gui.png");

    public MinerScreen(BaseStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("screen.simplestationsminer.miner");
    }

    @Override
    protected void renderMoreTooltips(GuiGraphics gfx, int mouseX, int mouseY, BaseStationBlockEntity station) {
        if (!(menu.blockEntity instanceof MinerBlockEntity miner))
            return;

        int startX = getStartX();
        int startY = getStartY();

        renderPowerTooltip(gfx, UIBlocks.FUEL_BAR, mouseX, mouseY, station);
        renderProgressTooltip(gfx, UIBlocks.PROGRESS_BAR, mouseX, mouseY, station);

        if (UIBlocks.COOL_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            var coolant = miner.resources.get(MinerBlockEntity.COOLANT_SLOT).get();
            String coolantPart = coolant + " / " + Config.MAX_COOLANT.get();
            List<Component> coolantText = Arrays.asList(Component.translatable("screen.simplestationsminer.coolant"),
                    Component.literal(coolantPart));
            gfx.renderComponentTooltip(font, coolantText, mouseX, mouseY);
        }

        if (UIBlocks.CATA_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            var redstone = miner.resources.get(MinerBlockEntity.REDSTONE_SLOT).get();
            var redstonePart = redstone + " / " + Config.MAX_CATALYST.get();
            List<Component> redstoneText = Arrays.asList(Component.translatable("screen.simplestationsminer.catalysis"),
                    Component.literal(redstonePart));
            gfx.renderComponentTooltip(font, redstoneText, mouseX, mouseY);
        }

        if (miner.type == -1 && !miner.invalidDepth
                && UIBlocks.FILTER_SLOT.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.filter"), mouseX, mouseY);
        }

        if (!miner.hasNetherUpgrade && UIBlocks.PORTAL_SLOT.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.need_portal"), mouseX, mouseY);
        }

        if (miner.drillCount == 0 && UIBlocks.DRILL_SLOT.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.need_drill"), mouseX, mouseY);
        }

        if (miner.invalidDepth && UIBlocks.ERROR.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.depthError"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float tick, int mx, int my) {
        super.renderBg(graphics, tick, mx, my);
        if (!(menu.blockEntity instanceof MinerBlockEntity miner))
            return;

        int x = getStartX();
        int y = getStartY();
        int tickAlpha = 96 + (int) (63 * Math.sin(System.currentTimeMillis() / 400.0));
        int borderColor = (tickAlpha << 24) | 0xFF0000;
        renderProgressBar(graphics, miner, UIBlocks.PROGRESS_BAR);

        if (miner.invalidDepth) {
            Component error = Component.literal("Y > " + Config.MAX_Y.get());
            UIBlocks.ERROR.drawTextRight(graphics, x, y, font, borderColor, error);
            return;
        }

        renderPowerBar(graphics, miner, UIBlocks.FUEL_BAR, UIBlocks.FUEL_SLOT);

        if (miner.drillCount == 0)
            UIBlocks.DRILL_SLOT.drawBorder(graphics, x, y, borderColor);
        if (!miner.isValidWorld())
            UIBlocks.PORTAL_SLOT.drawBorder(graphics, x, y, borderColor);

        var coolRes = miner.resources.get(MinerBlockEntity.COOLANT_SLOT);
        var coolantPart = coolRes.getPercent();
        UIBlocks.COOL_BAR.drawProgressToTop(graphics, x, y, coolantPart, 0xAA3333AA);

        var redRes = miner.resources.get(MinerBlockEntity.REDSTONE_SLOT);
        var redstonePart = redRes.getPercent();
        UIBlocks.CATA_BAR.drawProgressToTop(graphics, x, y, redstonePart, 0xAABB2211);

        if (!coolRes.isEnough())
            UIBlocks.COOL_SLOT.drawBorder(graphics, x, y, borderColor);
        if (!redRes.isEnough())
            UIBlocks.CATA_SLOT.drawBorder(graphics, x, y, borderColor);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}

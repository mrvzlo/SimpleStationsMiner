package com.ave.simplestationsminer;

import com.ave.simplestationsminer.blockentity.ModBlockEntities;
import com.ave.simplestationsminer.renderer.MinerRenderer;
import com.ave.simplestationsminer.screen.ModMenuTypes;
import com.ave.simplestationsminer.screen.MinerScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SimpleStationsMiner.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SimpleStationsMinerClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        MenuScreens.register(ModMenuTypes.MINER_MENU.get(), MinerScreen::new);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MINER_BLOCK_ENTITY.get(), MinerRenderer::new);
    }
}

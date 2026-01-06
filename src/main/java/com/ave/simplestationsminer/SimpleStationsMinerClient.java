package com.ave.simplestationsminer;

import com.ave.simplestationsminer.renderer.MinerRenderer;
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
    static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(Registrations.MINER_MENU.get(), MinerScreen::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registrations.MINER.getEntity(), MinerRenderer::new);
    }
}

package com.ave.simplestationsminer;

import org.slf4j.Logger;

import com.ave.simplestationscore.registrations.CoreRegistrations;
import com.mojang.logging.LogUtils;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleStationsMiner.MODID)
public class SimpleStationsMiner {
        public static final String MODID = "simplestationsminer";
        public static final Logger LOGGER = LogUtils.getLogger();

        public SimpleStationsMiner(FMLJavaModLoadingContext context) {
                IEventBus modEventBus = context.getModEventBus();
                context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
                Registrations.MANAGER.register(modEventBus);
                modEventBus.addListener(this::addCreative);
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (!event.getTab().equals(CoreRegistrations.CREATIVE_TAB.get()))
                        return;
                event.accept(Registrations.MINER.getItem());
                event.accept(Registrations.DRILL_ITEM.get());
                event.accept(Registrations.DRILL_ITEM_2.get());
                event.accept(Registrations.DRILL_ITEM_3.get());
                event.accept(Registrations.PORTAL.get());
        }
}
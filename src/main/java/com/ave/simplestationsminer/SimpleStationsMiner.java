package com.ave.simplestationsminer;

import org.slf4j.Logger;

import com.ave.simplestationscore.registrations.CoreRegistrations;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(SimpleStationsMiner.MODID)
public class SimpleStationsMiner {
        public static final String MODID = "simplestationsminer";
        public static final Logger LOGGER = LogUtils.getLogger();

        public SimpleStationsMiner(IEventBus modEventBus, ModContainer modContainer) {
                modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
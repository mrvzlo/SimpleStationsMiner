package com.ave.simplestationsminer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = SimpleStationsMiner.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        static ForgeConfigSpec SPEC;

        public static ForgeConfigSpec.IntValue ENERGY_PER_TICK;
        public static ForgeConfigSpec.IntValue MAX_PROGRESS;
        public static ForgeConfigSpec.IntValue MAX_COOLANT;
        public static ForgeConfigSpec.IntValue MAX_CATALYST;

        public static ForgeConfigSpec.IntValue FUEL_PER_COAL;
        public static ForgeConfigSpec.IntValue FUEL_CAPACITY;
        public static ForgeConfigSpec.IntValue MAX_Y;

        public static ForgeConfigSpec.IntValue COOLANT_USAGE;
        public static ForgeConfigSpec.IntValue CATALYST_USAGE;

        static {
                setupGenerationConfig();
                SPEC = BUILDER.build();
        }

        private static void setupGenerationConfig() {
                ENERGY_PER_TICK = BUILDER
                                .comment("How much RF to consume per tick\n Default: 16")
                                .defineInRange("consume", 16, 1, 1000);
                MAX_PROGRESS = BUILDER
                                .comment("Default working time in ticks\n Default: 1000")
                                .defineInRange("work_time", 1000, 1, 100000);
                MAX_COOLANT = BUILDER
                                .comment("Max coolant to store\n Default: 20")
                                .defineInRange("max_coolant", 20, 1, 10000);
                MAX_CATALYST = BUILDER
                                .comment("Max catalyst to store\n Default: 20")
                                .defineInRange("max_catalyst", 20, 1, 10000);
                FUEL_PER_COAL = BUILDER
                                .comment("Base RF amount received from 1 coal\n Default: 48000")
                                .defineInRange("fuel_rf", 48000, 1, 1000000);
                FUEL_CAPACITY = BUILDER
                                .comment("How much RF can be stored\n Default: 480000")
                                .defineInRange("fuel_max", 480000, 1, 2_000_000_000);
                MAX_Y = BUILDER
                                .comment("Highest Y for miner\n Default: 20")
                                .defineInRange("max_y", 20, -1000, 1000);
                COOLANT_USAGE = BUILDER
                                .comment("Coolant usage per ore\n Default: 1")
                                .defineInRange("coolant_usage", 1, 0, 64);
                CATALYST_USAGE = BUILDER
                                .comment("Catalyst usage per ore\n Default: 1")
                                .defineInRange("catalyst_usage", 1, 0, 64);
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
        }
}

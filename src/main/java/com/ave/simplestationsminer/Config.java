package com.ave.simplestationsminer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = SimpleStationsMiner.MODID)
public class Config {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        static ModConfigSpec SPEC;

        public static ModConfigSpec.IntValue ENERGY_PER_TICK;
        public static ModConfigSpec.IntValue MAX_PROGRESS;
        public static ModConfigSpec.IntValue MAX_COOLANT;
        public static ModConfigSpec.IntValue MAX_CATALYST;

        public static ModConfigSpec.IntValue MAX_Y;

        public static ModConfigSpec.IntValue COOLANT_USAGE;
        public static ModConfigSpec.IntValue CATALYST_USAGE;

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

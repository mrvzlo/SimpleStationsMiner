package com.ave.simplestationsminer;

import org.slf4j.Logger;

import com.ave.simplestationsminer.blockentity.ModBlockEntities;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.partblock.PartBlock;
import com.ave.simplestationsminer.screen.ModMenuTypes;
import com.ave.simplestationsminer.sound.ModSounds;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleStationsMiner.MODID)
public class SimpleStationsMiner {
        public static final String MODID = "simplestationsminer";
        public static final Logger LOGGER = LogUtils.getLogger();
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, MODID);

        public static final RegistryObject<Block> MINER_BLOCK = BLOCKS.register("miner",
                        () -> new MinerBlock(BlockBehaviour.Properties.of()
                                        .strength(0.1F).lightLevel((state) -> 12).noOcclusion()));

        public static final RegistryObject<Block> MINER_PART = BLOCKS.register("miner_part",
                        () -> new PartBlock(BlockBehaviour.Properties.of()
                                        .strength(0.1F).noOcclusion()));

        public static final RegistryObject<BlockItem> MINER_BLOCK_ITEM = ITEMS.register(
                        "miner", () -> new ItemNameBlockItem(MINER_BLOCK.get(), new Item.Properties()));

        public static final RegistryObject<Item> MINER_DRILL = ITEMS.register(
                        "miner_drill", () -> new Item(new Item.Properties()));

        public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
                        .register("example_tab", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.simplestationsminer")) // The language
                                                                                                        // key for
                                        // the title of your
                                        // CreativeModeTab
                                        .withTabsBefore(CreativeModeTabs.COMBAT)
                                        .icon(() -> MINER_BLOCK_ITEM.get().getDefaultInstance())
                                        .displayItems((parameters, output) -> {
                                                output.accept(MINER_BLOCK_ITEM.get());
                                                output.accept(MINER_DRILL.get());
                                        }).build());

        public static final Capability<IItemHandler> ITEM = CapabilityManager.get(new CapabilityToken<>() {
        });

        public static final Capability<IEnergyStorage> ENERGY = CapabilityManager.get(new CapabilityToken<>() {
        });

        public SimpleStationsMiner(FMLJavaModLoadingContext context) {
                IEventBus modEventBus = context.getModEventBus();
                context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
                BLOCKS.register(modEventBus);
                ITEMS.register(modEventBus);
                CREATIVE_MODE_TABS.register(modEventBus);
                ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
                ModMenuTypes.register(modEventBus);
                ModSounds.SOUND_EVENTS.register(modEventBus);

                modEventBus.addListener(this::addCreative);
        }

        // Add the example block item to the building blocks tab
        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
                        event.accept(MINER_BLOCK_ITEM);
        }
}
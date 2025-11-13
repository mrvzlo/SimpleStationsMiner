package com.ave.smartminer;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SmartMiner.MODID)
public class SmartMiner {
    public static final String MODID = "smartminer";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> IRON_MINER_BLOCK = BLOCKS.register("iron_miner",
            () -> new AutoMinerBlock(BlockBehaviour.Properties.of()
                    .strength(4F)
                    .requiresCorrectToolForDrops(), AutoMinerType.IRON));

    public static final DeferredItem<BlockItem> IRON_MINER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("iron_miner",
            IRON_MINER_BLOCK);

    public static final DeferredBlock<Block> GOLD_MINER_BLOCK = BLOCKS.register("gold_miner",
            () -> new AutoMinerBlock(BlockBehaviour.Properties.of()
                    .strength(4F)
                    .requiresCorrectToolForDrops(), AutoMinerType.GOLD));

    public static final DeferredItem<BlockItem> GOLD_MINER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("gold_miner",
            GOLD_MINER_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
            .register("example_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.smartminer")) // The language key for the title of your
                                                                           // CreativeModeTab
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> IRON_MINER_BLOCK_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(IRON_MINER_BLOCK_ITEM.get());
                        output.accept(GOLD_MINER_BLOCK_ITEM.get());
                    }).build());

    public SmartMiner(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(IRON_MINER_BLOCK_ITEM);
    }

}

package com.ave.simplestationsminer.datagen;

import java.util.List;
import java.util.Set;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

public class ModBlockLootTable extends BlockLootSubProvider {
    protected ModBlockLootTable() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(SimpleStationsMiner.MINER_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(SimpleStationsMiner.MINER_BLOCK.get());
    }
}

package com.ave.simplestationsminer.datagen;

import java.util.List;
import java.util.Set;

import com.ave.simplestationsminer.Registrations;

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
        dropSelf(Registrations.MINER.getBlock());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(Registrations.MINER.getBlock());
    }
}

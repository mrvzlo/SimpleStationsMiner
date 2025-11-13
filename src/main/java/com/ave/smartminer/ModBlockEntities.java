package com.ave.smartminer;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, SmartMiner.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AutoMinerBlockEntity>> MINER_BLOCK_ENTITY = BLOCK_ENTITIES
            .register("iron_miner",
                    () -> BlockEntityType.Builder.of(AutoMinerBlockEntity::new, SmartMiner.IRON_MINER_BLOCK.get())
                            .build(null));

}
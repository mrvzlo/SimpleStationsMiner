package com.ave.smartminer.blockentity;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(Registries.BLOCK_ENTITY_TYPE, SmartMiner.MODID);

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SmartMinerBlockEntity>> MINER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("smart_miner",
                                        () -> BlockEntityType.Builder
                                                        .of(SmartMinerBlockEntity::new,
                                                                        SmartMiner.Smart_MINER_BLOCK.get())
                                                        .build(null));

}
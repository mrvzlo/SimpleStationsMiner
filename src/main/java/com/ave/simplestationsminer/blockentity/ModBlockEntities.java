package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.partblock.PartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(Registries.BLOCK_ENTITY_TYPE, SimpleStationsMiner.MODID);

        public static final RegistryObject<BlockEntityType<MinerBlockEntity>> MINER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("miner",
                                        () -> BlockEntityType.Builder
                                                        .of(MinerBlockEntity::new,
                                                                        SimpleStationsMiner.MINER_BLOCK.get())
                                                        .build(null));

        public static final RegistryObject<BlockEntityType<PartBlockEntity>> PART_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("miner_part",
                                        () -> BlockEntityType.Builder
                                                        .of(PartBlockEntity::new,
                                                                        SimpleStationsMiner.MINER_PART.get())
                                                        .build(null));
}
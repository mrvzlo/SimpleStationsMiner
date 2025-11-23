package com.ave.simplestationsminer.sound;

import java.util.function.Supplier;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
        public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
                        .create(ForgeRegistries.SOUND_EVENTS, SimpleStationsMiner.MODID);

        public static final Supplier<SoundEvent> WORK_SOUND = SOUND_EVENTS.register(
                        "work_sound",
                        () -> SoundEvent
                                        .createVariableRangeEvent(ResourceLocation
                                                        .fromNamespaceAndPath(SimpleStationsMiner.MODID,
                                                                        "work_sound")));

}

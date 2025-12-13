package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class SoundManager {
    public static void playSound(MinerBlockEntity miner) {
        if (miner.soundCooldown > 0) {
            miner.soundCooldown--;
            return;
        }
        miner.soundCooldown += 25;
        miner.getLevel().playSound(null, miner.getBlockPos(), SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS);
    }
}

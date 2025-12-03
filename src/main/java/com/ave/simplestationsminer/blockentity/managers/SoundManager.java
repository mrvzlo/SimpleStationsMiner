package com.ave.simplestationsminer.blockentity.managers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.sound.ModSounds;

import net.minecraft.sounds.SoundSource;

public class SoundManager {
    public static void playSound(MinerBlockEntity miner) {
        if (miner.soundCooldown > 0) {
            miner.soundCooldown--;
            return;
        }
        miner.soundCooldown += 25;
        miner.getLevel().playSound(null, miner.getBlockPos(), ModSounds.WORK_SOUND.get(), SoundSource.BLOCKS);
    }
}

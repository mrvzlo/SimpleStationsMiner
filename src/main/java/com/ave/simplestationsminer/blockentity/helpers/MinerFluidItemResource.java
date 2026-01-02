package com.ave.simplestationsminer.blockentity.helpers;

import com.ave.simplestationscore.resources.FluidItemResource;

public class MinerFluidItemResource extends FluidItemResource {

    public MinerFluidItemResource(int max, int usage, String key) {
        super(max, usage, 1, key);
    }

    @Override
    public boolean useEveryTick() {
        return false;
    }
}

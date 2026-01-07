package com.ave.simplestationsminer.blockentity.helpers;

import com.ave.simplestationscore.resources.FluidItemResource;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MinerFluidItemResource extends FluidItemResource {

    public MinerFluidItemResource(int max, int usage, String key) {
        super(max, usage, 1, key);
    }

    @Override
    public boolean useEveryTick() {
        return false;
    }

    @Override
    public int getIncrement(Item item) {
        return item.equals(Items.REDSTONE_BLOCK) || item.equals(Items.LAPIS_BLOCK) ? 9 : 1;
    }
}

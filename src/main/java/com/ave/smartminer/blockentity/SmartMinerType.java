package com.ave.smartminer.blockentity;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum SmartMinerType implements StringRepresentable {
    Unknown(null),
    IRON(Items.RAW_IRON),
    GOLD(Items.RAW_GOLD);

    public final Item minedItem;

    SmartMinerType(Item item) {
        this.minedItem = item;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}

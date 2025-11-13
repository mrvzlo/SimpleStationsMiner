package com.ave.smartminer;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum AutoMinerType implements StringRepresentable {
    Unknown(null),
    IRON(Items.RAW_IRON),
    GOLD(Items.RAW_GOLD);

    public final Item minedItem;

    AutoMinerType(Item item) {
        this.minedItem = item;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}

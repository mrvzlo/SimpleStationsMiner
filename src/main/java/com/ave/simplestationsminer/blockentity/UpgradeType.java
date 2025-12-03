package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.SimpleStationsMiner;
import net.minecraft.world.item.Item;

public enum UpgradeType {
    Unknown(null),
    Portal(SimpleStationsMiner.PORTAL.get()),
    Drill1(SimpleStationsMiner.DRILL_ITEM.get()),
    Drill2(SimpleStationsMiner.DRILL_ITEM_2.get()),
    Drill3(SimpleStationsMiner.DRILL_ITEM_3.get()),;

    public final Item item;

    private UpgradeType(Item item) {
        this.item = item;
    }

    public static UpgradeType getFromItem(Item item) {
        for (var c : values())
            if (item.equals(c.item))
                return c;

        return Unknown;
    }
}

package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.Registrations;
import net.minecraft.world.item.Item;

public enum UpgradeType {
    Unknown(null),
    Portal(Registrations.PORTAL.get()),
    Drill1(Registrations.DRILL_ITEM.get()),
    Drill2(Registrations.DRILL_ITEM_2.get()),
    Drill3(Registrations.DRILL_ITEM_3.get()),;

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

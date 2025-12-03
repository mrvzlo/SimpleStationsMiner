package com.ave.simplestationsminer.blockentity.managers;

import java.util.ArrayList;
import java.util.List;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;
import com.ave.simplestationsminer.blockentity.UpgradeType;

import net.minecraft.world.item.Item;

public class UpgradeManager {
    public static boolean checkUpgradeSlots(MinerBlockEntity miner) {
        var newUpgrades = getUpgradeList(miner);
        var newHash = getUpgradesHash(newUpgrades);
        if (newHash == miner.upgradesHash)
            return false;

        miner.upgrades = newUpgrades;
        applyUpgrades(miner);
        miner.speed = WorkManager.getSpeedValue(miner);
        return true;
    }

    private static List<UpgradeType> getUpgradeList(MinerBlockEntity miner) {
        var list = new ArrayList<UpgradeType>();
        var portalSlot = miner.inventory.getStackInSlot(ModContainer.PORTAL_SLOT);
        if (portalSlot.getCount() > 0)
            list.add(UpgradeType.Portal);

        var drillSlot = miner.inventory.getStackInSlot(ModContainer.DRILL_SLOT);
        if (drillSlot.getCount() > 0) {
            var type = UpgradeType.getFromItem(drillSlot.getItem());
            for (var i = 0; i < drillSlot.getCount(); i++)
                list.add(type);
        }
        return list;
    }

    public static void applyUpgrades(MinerBlockEntity miner) {
        miner.hasNetherUpdate = false;
        miner.drill = null;
        miner.drillCount = 0;

        for (var up : miner.upgrades) {
            if (up.equals(UpgradeType.Portal))
                miner.hasNetherUpdate = true;
            else {
                miner.drillCount++;
                miner.drill = up.item;
            }
        }

        miner.upgradesHash = getUpgradesHash(miner.upgrades);
        miner.powerConsuption = getPowerConsumption(miner.drill);
    }

    private static int getUpgradesHash(List<UpgradeType> list) {
        var hash = 0;
        for (var up : list) {
            hash *= 31;
            hash += up.ordinal();
        }
        return hash;
    }

    private static int getPowerConsumption(Item drill) {
        if (drill == null)
            return 0;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM.get()))
            return 1;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM_2.get()))
            return 2;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM_3.get()))
            return 4;
        return 0;
    }

    public static float getSpeedPerDrill(Item drill) {
        if (drill == null)
            return 0;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM.get()))
            return 0.5f;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM_2.get()))
            return 0.9f;
        if (drill.equals(SimpleStationsMiner.DRILL_ITEM_3.get()))
            return 1.5f;
        return 0;
    }
}

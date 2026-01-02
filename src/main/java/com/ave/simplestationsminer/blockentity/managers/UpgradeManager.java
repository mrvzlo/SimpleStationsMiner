package com.ave.simplestationsminer.blockentity.managers;

import java.util.ArrayList;
import java.util.List;

import com.ave.simplestationscore.resources.EnergyResource;
import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.Registrations;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.UpgradeType;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
        var portalSlot = miner.inventory.getStackInSlot(MinerBlockEntity.PORTAL_SLOT);
        if (portalSlot.getCount() > 0)
            list.add(UpgradeType.Portal);

        var drillSlot = miner.inventory.getStackInSlot(MinerBlockEntity.DRILL_SLOT);
        if (drillSlot.getCount() > 0) {
            var type = UpgradeType.getFromItem(drillSlot.getItem());
            for (var i = 0; i < drillSlot.getCount(); i++)
                list.add(type);
        }
        return list;
    }

    public static void applyUpgrades(MinerBlockEntity miner) {
        miner.hasNetherUpgrade = false;
        miner.drill = UpgradeType.Unknown;
        miner.drillCount = 0;

        for (var up : miner.upgrades) {
            if (up.equals(UpgradeType.Portal))
                miner.hasNetherUpgrade = true;
            else {
                miner.drillCount++;
                miner.drill = up;
            }
        }

        miner.upgradesHash = getUpgradesHash(miner.upgrades);
        var energy = (EnergyResource) miner.resources.get(MinerBlockEntity.FUEL_SLOT);
        energy.usage = getPowerConsumption(miner.drill.item);
    }

    public static int getPowerConsumption(Item drill) {
        var base = Config.ENERGY_PER_TICK.get();
        if (drill == null)
            return 0;
        if (drill.equals(Registrations.DRILL_ITEM.get()))
            return base;
        if (drill.equals(Registrations.DRILL_ITEM_2.get()))
            return base * 2;
        if (drill.equals(Registrations.DRILL_ITEM_3.get()))
            return base * 4;
        return 0;
    }

    private static int getUpgradesHash(List<UpgradeType> list) {
        var hash = 0;
        for (var up : list) {
            hash *= 31;
            hash += up.ordinal();
        }
        return hash;
    }

    public static float getSpeedPerDrill(Item drill) {
        if (drill == null)
            return 0;
        if (drill.equals(Registrations.DRILL_ITEM.get()))
            return 0.5f;
        if (drill.equals(Registrations.DRILL_ITEM_2.get()))
            return 0.9f;
        if (drill.equals(Registrations.DRILL_ITEM_3.get()))
            return 1.5f;
        return 0;
    }

    public static UpgradeType getMinDrill(Item ore) {
        if (ore.equals(Items.OBSIDIAN))
            return UpgradeType.Drill2;
        if (ore.equals(Items.ANCIENT_DEBRIS))
            return UpgradeType.Drill2;

        return UpgradeType.Drill1;
    }
}

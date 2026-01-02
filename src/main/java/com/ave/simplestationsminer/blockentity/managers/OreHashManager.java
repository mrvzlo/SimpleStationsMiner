package com.ave.simplestationsminer.blockentity.managers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ave.simplestationsminer.datagen.ModTags;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OreHashManager {
    private static Map<Item, Integer> itemToInt = new IdentityHashMap<>();
    private static Map<Integer, Item> intToItem = new HashMap<>();
    private static Set<Integer> netherOres = new HashSet<>();
    private static boolean initialized = false;

    public static ItemStack getItemStack(int type) {
        if (!initialized)
            init();

        if (type == -1)
            return ItemStack.EMPTY;
        var product = OreHashManager.intToItem.get(type);

        if (product == null)
            return ItemStack.EMPTY;
        var count = WorkManager.getOutputSize(product);
        return new ItemStack(product, count);
    }

    public static int getHash(ItemStack stack) {
        if (!initialized)
            init();

        if (stack.isEmpty())
            return -1;
        var item = stack.getItem();
        var hash = OreHashManager.itemToInt.get(item);
        return hash == null ? -1 : hash;
    }

    public static boolean isRegular(ItemStack stack) {
        return isRegular(getHash(stack));
    }

    public static boolean isRegular(int type) {
        if (!initialized)
            init();

        return type == -1 || !netherOres.contains(type);
    }

    public static void init() {
        initialized = true;
        var ores = BuiltInRegistries.ITEM.getTag(ModTags.Items.MINEABLE_TAG)
                .map(h -> h.stream().map(holder -> holder.value()).toList())
                .orElse(List.of());

        for (var item : ores) {
            var id = item.toString().hashCode();
            itemToInt.put(item, id);
            intToItem.put(id, item);
            var isNether = item.toString().contains("nether") || item.equals(Items.ANCIENT_DEBRIS);
            if (isNether)
                netherOres.add(id);
        }
    }
}

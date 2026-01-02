package com.ave.simplestationsminer;

import com.ave.simplestationscore.registrations.RegistrationManager;
import com.ave.simplestationscore.registrations.Station;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.screen.MinerMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class Registrations {
        public static final RegistrationManager MANAGER = new RegistrationManager(SimpleStationsMiner.MODID);

        public static final Station<MinerBlockEntity, MinerBlock> MINER = MANAGER.registerStation(
                        "miner", (p) -> new MinerBlock(p), MinerBlockEntity::new);

        public static final DeferredItem<Item> PORTAL = MANAGER.ITEMS.registerItem("portal", Item::new,
                        new Item.Properties());
        public static final DeferredItem<Item> DRILL_ITEM = MANAGER.ITEMS.registerItem("drill", Item::new,
                        new Item.Properties());
        public static final DeferredItem<Item> DRILL_ITEM_2 = MANAGER.ITEMS.registerItem("drill_2", Item::new,
                        new Item.Properties());
        public static final DeferredItem<Item> DRILL_ITEM_3 = MANAGER.ITEMS.registerItem("drill_3", Item::new,
                        new Item.Properties());

        public static final DeferredHolder<MenuType<?>, MenuType<MinerMenu>> MINER_MENU = MANAGER.registerMenuType(
                        "miner_menu", MinerMenu::new);

}

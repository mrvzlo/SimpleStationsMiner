package com.ave.simplestationsminer;

import com.ave.simplestationscore.registrations.RegistrationManager;
import com.ave.simplestationscore.registrations.Station;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.screen.MinerMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class Registrations {
        public static final RegistrationManager MANAGER = new RegistrationManager(SimpleStationsMiner.MODID);

        public static final Station<MinerBlockEntity, MinerBlock> MINER = MANAGER.registerStation(
                        "miner", (p) -> new MinerBlock(p), MinerBlockEntity::new);

        public static final RegistryObject<Item> PORTAL = MANAGER.ITEMS.register("portal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> DRILL_ITEM = MANAGER.ITEMS.register("drill",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> DRILL_ITEM_2 = MANAGER.ITEMS.register("drill_2",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> DRILL_ITEM_3 = MANAGER.ITEMS.register("drill_3",
                        () -> new Item(new Item.Properties()));

        public static final RegistryObject<MenuType<MinerMenu>> MINER_MENU = MANAGER.registerMenuType(
                        "miner_menu", MinerMenu::new);

}

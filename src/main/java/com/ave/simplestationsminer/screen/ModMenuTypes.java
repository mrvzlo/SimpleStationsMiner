package com.ave.simplestationsminer.screen;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister
            .create(net.minecraft.core.registries.Registries.MENU, SimpleStationsMiner.MODID);

    public static final RegistryObject<MenuType<MinerMenu>> MINER_MENU = MENUS.register("miner_menu",
            () -> IForgeMenuType.create(MinerMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}

package com.ave.simplestationsminer;

import com.ave.simplestationsminer.screen.MinerScreen;
import com.ave.simplestationsminer.screen.MinerScreenHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SimpleStationsMinerClient implements ClientModInitializer {

	public static final ScreenHandlerType<MinerScreenHandler> MINER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
			Identifier.of(SimpleStationsMiner.MOD_ID, "miner_menu"),
			new ExtendedScreenHandlerType<>(MinerScreenHandler::new, BlockPos.PACKET_CODEC));

	@Override
	public void onInitializeClient() {
		HandledScreens.register(MINER_SCREEN_HANDLER, MinerScreen::new);

	}
}
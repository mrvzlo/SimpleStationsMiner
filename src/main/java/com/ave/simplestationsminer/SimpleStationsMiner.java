package com.ave.simplestationsminer;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ave.simplestationsminer.registrations.Registrations;

public class SimpleStationsMiner implements ModInitializer {
	public static final String MOD_ID = "simplestationsminer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Registrations.initialize();

		LOGGER.info("Hello Fabric world!");
	}
}
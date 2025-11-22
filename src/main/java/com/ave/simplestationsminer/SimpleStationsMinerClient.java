package com.ave.simplestationsminer;

import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.renderer.MinerRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SimpleStationsMinerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(Registrations.MINER_BLOCK_ENTITY, MinerRenderer::new);
	}
}

package com.ofekn.crafting_on_a_stick.network;

import com.ofekn.crafting_on_a_stick.CraftingOnAStick;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class COASPacketHandler {
	private COASPacketHandler() {}
	public static void loadClass() {}

	private static final String PROTOCOL_VERSION = "1.21.0.1";
	
	@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.MOD)
	private static final class Register {
		@SubscribeEvent
		public static void event(RegisterPayloadHandlersEvent event) {
			PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
			registrar.commonToServer(SBOpenCurios.TYPE, SBOpenCurios.CODEC, SBOpenCurios::handle);
		}
	}
}

package com.ofekn.crafting_on_a_stick;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.MOD)
public final class COASConfig {
	private COASConfig() {}

	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue STORE_ITEMS = BUILDER
			.comment("Whether to store items in the crafting stations when they are closed")
			.define("storeItems", true);

	static final ModConfigSpec SPEC = BUILDER.build();

	private static boolean storeItems;

	public static boolean getStoreItems() {
		return storeItems;
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		storeItems = STORE_ITEMS.get();
	}
}

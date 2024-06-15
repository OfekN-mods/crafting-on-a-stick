package com.ofekn.crafting_on_a_stick;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
final class ClientModEvents {
	@SubscribeEvent
	public static void event(BuildCreativeModeTabContentsEvent event) {
		if (CreativeModeTabs.TOOLS_AND_UTILITIES.equals(event.getTabKey())) {
			event.accept(ModItems.CRAFTING_TABLE);
			event.accept(ModItems.LOOM);
			event.accept(ModItems.GRINDSTONE);
			event.accept(ModItems.CARTOGRAPHY_TABLE);
			event.accept(ModItems.STONECUTTER);
			event.accept(ModItems.SMITHING_TABLE);
			event.accept(ModItems.ANVIL);
			event.accept(ModItems.CHIPPED_ANVIL);
			event.accept(ModItems.DAMAGED_ANVIL);
		}
	}
}

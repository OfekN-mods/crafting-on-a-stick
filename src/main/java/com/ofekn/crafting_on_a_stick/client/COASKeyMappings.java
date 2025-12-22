package com.ofekn.crafting_on_a_stick.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.ofekn.crafting_on_a_stick.CraftingOnAStick;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class COASKeyMappings {
    public static final KeyMapping OPEN_CURIOS_KEY = new KeyMapping("crafting_on_a_stick.key.open_curios", InputConstants.KEY_V, "key.categories.inventory");

	@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	private static class ForgeEvents {
		@SubscribeEvent
		public static void event(ClientTickEvent.Pre event) {
            if (!OPEN_CURIOS_KEY.consumeClick()) {
                return;
            }
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.screen != null) {
                return;
            }
			Player player = minecraft.player;
			if (player == null) {
                return;
            }
            COASWheelScreen.trigger(minecraft, player);
		}
	}

	@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	private static class ModEvents {
		@SubscribeEvent
		public static void event(RegisterKeyMappingsEvent event) {
			event.register(OPEN_CURIOS_KEY);
		}
	}
}

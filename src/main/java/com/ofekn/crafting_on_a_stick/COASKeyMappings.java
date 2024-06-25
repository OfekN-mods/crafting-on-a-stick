package com.ofekn.crafting_on_a_stick;

import com.mojang.blaze3d.platform.InputConstants;
import com.ofekn.crafting_on_a_stick.network.SBOpenCurios;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class COASKeyMappings {
	private static ModKeys keys;

	public static ModKeys getKeys() {
		return keys;
	}

	@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
	private static class ForgeEvents {
		@SubscribeEvent
		public static void event(ClientTickEvent.Pre event) {
			boolean openCurios = keys.OPEN_CURIOS_KEY.consumeClick();
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.screen != null)
				return;
			Player player = minecraft.player;
			if (player == null)
				return;

			if (openCurios)
				PacketDistributor.sendToServer(SBOpenCurios.INSTANCE);
		}
	}

	@EventBusSubscriber(modid = CraftingOnAStick.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	private static class ModEvents {
		@SubscribeEvent
		public static void event(RegisterKeyMappingsEvent event) {
			keys = new ModKeys();
			event.register(keys.OPEN_CURIOS_KEY);
		}
	}

	//This class is inorder so KeyMapping won't get loaded on server side
	public static final class ModKeys {
		public final KeyMapping OPEN_CURIOS_KEY = new KeyMapping("crafting_on_a_stick.key.open_curios", InputConstants.KEY_V, "key.categories.inventory");
		private ModKeys() {}
	}
}

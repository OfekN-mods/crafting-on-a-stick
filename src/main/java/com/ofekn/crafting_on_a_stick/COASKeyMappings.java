package com.ofekn.crafting_on_a_stick;

//import com.mojang.blaze3d.platform.InputConstants;
//import com.ofek2608.crafting_on_a_stick.network.COASPacketHandler;
//import net.minecraft.client.KeyMapping;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
//import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//public class COASKeyMappings {
//	private static ModKeys keys;
//
//	public static ModKeys getKeys() {
//		return keys;
//	}
//
//	@Mod.EventBusSubscriber(modid = CraftingOnAStick.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//	private static class ForgeEvents {
//		@SubscribeEvent
//		public static void event(TickEvent.ClientTickEvent event) {
//			if (event.phase != TickEvent.Phase.START)
//				return;
//			boolean openCurios = keys.OPEN_CURIOS_KEY.consumeClick();
//			Minecraft minecraft = Minecraft.getInstance();
//			if (minecraft.screen != null)
//				return;
//			Player player = minecraft.player;
//			if (player == null)
//				return;
//
//			if (openCurios)
//				COASPacketHandler.sbOpenCurios();
//		}
//	}
//
//	@Mod.EventBusSubscriber(modid = CraftingOnAStick.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//	private static class ModEvents {
//		@SubscribeEvent
//		public static void event(RegisterKeyMappingsEvent event) {
//			keys = new ModKeys();
//			event.register(keys.OPEN_CURIOS_KEY);
//		}
//	}
//
//	//This class is inorder so KeyMapping won't get loaded on server side
//	public static final class ModKeys {
//		public final KeyMapping OPEN_CURIOS_KEY = new KeyMapping("crafting_on_a_stick.key.open_curios", InputConstants.KEY_V, "key.categories.inventory");
//		private ModKeys() {}
//	}
//}

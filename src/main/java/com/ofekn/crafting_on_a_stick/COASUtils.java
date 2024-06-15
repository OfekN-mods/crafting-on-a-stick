package com.ofekn.crafting_on_a_stick;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;

public final class COASUtils {
	private COASUtils() {}
	
	public static IItemHandlerModifiable getFullInventory(Player player) {
		return new PlayerInvWrapper(player.getInventory());
//		IItemHandlerModifiable inventory = new PlayerInvWrapper(player.getInventory());
//		Optional<IItemHandlerModifiable> curios = COASCurios.getCuriosInventory(player);
//		return curios.isEmpty() ? inventory : new CombinedInvWrapper(inventory, curios.get());
	}
	
}

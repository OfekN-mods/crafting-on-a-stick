package com.ofekn.crafting_on_a_stick;

import com.ofekn.crafting_on_a_stick.integration.COASCurios;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;

import java.util.Optional;

public final class COASUtils {
	private COASUtils() {}
	
	public static IItemHandlerModifiable getFullInventory(Player player) {
		// used when curios isn't updated
//		return new PlayerInvWrapper(player.getInventory());
		IItemHandlerModifiable inventory = new PlayerInvWrapper(player.getInventory());
		Optional<IItemHandlerModifiable> curios = COASCurios.getCuriosInventory(player);
		return curios.isEmpty() ? inventory : new CombinedInvWrapper(inventory, curios.get());
	}
	
}

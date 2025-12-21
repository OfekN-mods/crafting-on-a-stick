package com.ofekn.crafting_on_a_stick;

import com.ofekn.crafting_on_a_stick.integration.COASCurios;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class COASUtils {
	private COASUtils() {}
	
	public static List<Ref<ItemStack>> getFullInventory(Player player) {
		// used when curios isn't updated
//		return new PlayerInvWrapper(player.getInventory());
        List<Ref<ItemStack>> result = new ArrayList<>();
        var inventory = player.getInventory();
        Ref.forEveryIndex(
                inventory::getItem,
                inventory::setItem,
                inventory.getContainerSize(),
                result
        );
		COASCurios.getCuriosInventory(player, result);
		return result;
	}
	
}

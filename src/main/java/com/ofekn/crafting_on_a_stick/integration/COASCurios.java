package com.ofekn.crafting_on_a_stick.integration;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class COASCurios {
	private static final String MODID = "curios";
	private static final String SLOT_ID = "crafting_on_a_stick";

	public static boolean hasMod() {
		return ModList.get().isLoaded(MODID);
	}

	public static Optional<IItemHandlerModifiable> getCuriosInventory(Player player) {
		if (hasMod())
			return Integrator.getCuriosInventory(player);
		return Optional.empty();
	}

	private static final class Integrator {
		private Integrator() {}

		private static Optional<IItemHandlerModifiable> getCuriosInventory(Player player) {
			// using deprecated methods to hopefully also support Curios API Continuation
			Optional<ICuriosItemHandler> itemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
			if (itemHandler.isEmpty())
				return Optional.empty();
			Optional<ICurioStacksHandler> stackHandler = itemHandler.get().getStacksHandler(SLOT_ID);
			return stackHandler.map(ICurioStacksHandler::getStacks);
		}
	}
}

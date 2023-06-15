package com.ofek2608.crafting_on_a_stick.integration;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class COASCurios {
	@SuppressWarnings("EmptyMethod") public static void load() {}
	
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
	
	public static IItemHandlerModifiable getFullInventory(Player player) {
		IItemHandlerModifiable inventory = new PlayerInvWrapper(player.getInventory());
		Optional<IItemHandlerModifiable> curios = getCuriosInventory(player);
		return curios.isEmpty() ? inventory : new CombinedInvWrapper(inventory, curios.get());
	}
	
	private static final class Integrator {
		private Integrator() {}
		
		private static Optional<IItemHandlerModifiable> getCuriosInventory(Player player) {
			Optional<ICuriosItemHandler> itemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).resolve();
			if (itemHandler.isEmpty())
				return Optional.empty();
			Optional<ICurioStacksHandler> stackHandler = itemHandler.get().getStacksHandler(SLOT_ID);
			return stackHandler.map(ICurioStacksHandler::getStacks);
		}
	}
}

package com.ofek2608.crafting_on_a_stick.integration;

import com.ofek2608.crafting_on_a_stick.CraftingOnAStick;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
//import top.theillusivec4.curios.api.CuriosApi;
//import top.theillusivec4.curios.api.SlotTypeMessage;
//import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
//import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class COASCurios {
	@SuppressWarnings("EmptyMethod") public static void load() {}
	
	private static final String MODID = "curios";
	private static final ResourceLocation CURIO_POCKET_ICON = CraftingOnAStick.loc("item/curios_on_a_stick");
	private static final String SLOT_ID = CraftingOnAStick.ID;
	
	public static boolean hasMod() {
		return ModList.get().isLoaded(MODID);
	}
	
	static {
		if (hasMod())
			Integrator.init();
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
		
		private static void init() {
//			InterModComms.sendTo(MODID, SlotTypeMessage.REGISTER_TYPE, ()->new SlotTypeMessage.Builder(SLOT_ID)
//					.icon(CURIO_POCKET_ICON)
//					.size(1)
//					.build()
//			);
		}
		
		private static Optional<IItemHandlerModifiable> getCuriosInventory(Player player) {
//			Optional<ICuriosItemHandler> itemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).resolve();
//			if (itemHandler.isEmpty())
//				return Optional.empty();
//			Optional<ICurioStacksHandler> stackHandler = itemHandler.get().getStacksHandler(SLOT_ID);
//			if (stackHandler.isEmpty())
//				return Optional.empty();
//			return Optional.of(stackHandler.get().getStacks());
			return Optional.empty();
		}
	}
}

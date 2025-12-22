package com.ofekn.crafting_on_a_stick.integration;

import com.ofekn.crafting_on_a_stick.Ref;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class COASCurios {
	private static final String MODID = "curios";

	public static boolean hasMod() {
		return ModList.get().isLoaded(MODID);
	}

	public static void getCuriosInventory(Player player, List<Ref<ItemStack>> result) {
        if (hasMod()) {
            Integrator.getCuriosInventory(player, result);
        }
	}

	private static final class Integrator {
		private Integrator() {}

		private static void getCuriosInventory(Player player, List<Ref<ItemStack>> result) {
			// using deprecated methods to hopefully also support Curios API Continuation
            CuriosApi.getCuriosInventory(player).ifPresent(itemHandler -> {
                for (ICurioStacksHandler stackHandler : itemHandler.getCurios().values()) {
                    var stacks = stackHandler.getStacks();
                    Ref.forEveryIndex(
                            stacks::getStackInSlot,
                            stacks::setStackInSlot,
                            stacks.getSlots(),
                            result
                    );
                }
            });
		}
	}
}

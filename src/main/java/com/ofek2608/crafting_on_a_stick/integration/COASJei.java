package com.ofek2608.crafting_on_a_stick.integration;

import com.ofek2608.crafting_on_a_stick.CraftingOnAStick;
import com.ofek2608.crafting_on_a_stick.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class COASJei implements IModPlugin {
	public static final ResourceLocation PLUGIN_UID = new ResourceLocation(CraftingOnAStick.ID, "debug");
	@Override
	public ResourceLocation getPluginUid() {
		return PLUGIN_UID;
	}
	
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.CRAFTING_TABLE.get()), RecipeTypes.CRAFTING);
		registration.addRecipeCatalyst(new ItemStack(ModItems.STONECUTTER.get()), RecipeTypes.STONECUTTING);
		registration.addRecipeCatalyst(new ItemStack(ModItems.ANVIL.get()), RecipeTypes.ANVIL);
		registration.addRecipeCatalyst(new ItemStack(ModItems.CHIPPED_ANVIL.get()), RecipeTypes.ANVIL);
		registration.addRecipeCatalyst(new ItemStack(ModItems.DAMAGED_ANVIL.get()), RecipeTypes.ANVIL);
		registration.addRecipeCatalyst(new ItemStack(ModItems.SMITHING_TABLE.get()), RecipeTypes.SMITHING);
	}
}

package com.ofekn.crafting_on_a_stick.integration;

import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.item.CoasItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class CoasJei implements IModPlugin {
    public static final Identifier PLUGIN_UID = Coas.id(Coas.MID);
    @Override
    public Identifier getPluginUid() {
        return PLUGIN_UID;
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RecipeTypes.CRAFTING, CoasItem.CRAFTING_TABLE.get());
        registration.addCraftingStation(RecipeTypes.GRINDSTONE, CoasItem.GRINDSTONE.get());
        registration.addCraftingStation(RecipeTypes.STONECUTTING, CoasItem.STONECUTTER.get());
        registration.addCraftingStation(RecipeTypes.ANVIL, CoasItem.ANVIL.get());
        registration.addCraftingStation(RecipeTypes.ANVIL, CoasItem.CHIPPED_ANVIL.get());
        registration.addCraftingStation(RecipeTypes.ANVIL, CoasItem.DAMAGED_ANVIL.get());
        registration.addCraftingStation(RecipeTypes.SMITHING, CoasItem.SMITHING_TABLE.get());
    }
}
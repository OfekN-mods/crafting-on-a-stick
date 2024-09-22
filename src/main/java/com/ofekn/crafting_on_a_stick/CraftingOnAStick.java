package com.ofekn.crafting_on_a_stick;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mod(CraftingOnAStick.ID)
public final class CraftingOnAStick {
    public static final String ID = "crafting_on_a_stick";
    
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
    
    public CraftingOnAStick(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.REGISTER.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, COASConfig.SPEC);
    }
}

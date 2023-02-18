package com.ofek2608.crafting_on_a_stick;

import com.ofek2608.crafting_on_a_stick.integration.COASCurios;
import com.ofek2608.crafting_on_a_stick.network.COASPacketHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mod(CraftingOnAStick.ID)
public final class CraftingOnAStick {
    public static final String ID = "crafting_on_a_stick";
    
    public static ResourceLocation loc(String path) {
        return new ResourceLocation(ID, path);
    }
    
    public CraftingOnAStick() {
        ModItems.load();
        COASCurios.load();
        COASPacketHandler.loadClass();
    }
}

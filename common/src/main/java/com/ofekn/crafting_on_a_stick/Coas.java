package com.ofekn.crafting_on_a_stick;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

public final class Coas {
    private Coas() {}

    public static final String MID = "crafting_on_a_stick";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MID, path);
    }

    @ApiStatus.Internal
    public static void init() {}
}

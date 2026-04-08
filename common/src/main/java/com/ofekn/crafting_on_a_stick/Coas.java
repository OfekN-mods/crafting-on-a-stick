package com.ofekn.crafting_on_a_stick;

import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.integration.CoasIntegrations;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

public final class Coas {
    private Coas() {}

    public static final String MID = "crafting_on_a_stick";
    private static final Logger LOGGER = LogUtils.getLogger();

    @ApiStatus.Internal
    public static void init() {
        LOGGER.info("Hello from Common init on {}! we are currently in a {} environment!", CoasIntegrations.PLATFORM.getPlatformName(), CoasIntegrations.PLATFORM.getEnvironmentName());
        LOGGER.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));
        if (CoasIntegrations.PLATFORM.isModLoaded(MID)) {
            LOGGER.info("Hello to " + MID);
        }
    }
}

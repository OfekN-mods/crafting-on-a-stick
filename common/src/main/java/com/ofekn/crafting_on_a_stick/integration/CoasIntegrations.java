package com.ofekn.crafting_on_a_stick.integration;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public final class CoasIntegrations {
    private CoasIntegrations() {}

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final IPlatformIntegration PLATFORM = load();
    public static final ICuriosIntegration CURIOS = mod("curios", ICuriosIntegration.DEFAULT, () -> CuriosIntegration.INSTANCE);
    public static final IJeiIntegration JEI = mod("jei", IJeiIntegration.DEFAULT, () -> JeiIntegration.INSTANCE);

    private static IPlatformIntegration load() {
        final IPlatformIntegration loadedService = ServiceLoader.load(IPlatformIntegration.class, CoasIntegrations.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load platform integration"));
        LOGGER.info("Loaded platform integration for {}", loadedService.getPlatformName());
        return loadedService;
    }

    private static <T> T mod(String modid, T fallback, Supplier<? extends T> integration) {
        if (PLATFORM.isModLoaded(modid)) {
            LOGGER.info("Loaded integration for {}", modid);
            return integration.get();
        }
        LOGGER.info("Using fallback since {} is not present.", modid);
        return fallback;
    }
}
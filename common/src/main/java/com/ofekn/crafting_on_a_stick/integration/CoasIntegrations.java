package com.ofekn.crafting_on_a_stick.integration;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CoasIntegrations {
    private CoasIntegrations() {}

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final IPlatformIntegration PLATFORM = loadPlatform();
    public static final List<IInventoryExtender> INVENTORY_EXTENDERS = getInventoryExtenders();
    public static final IJeiIntegration JEI = mod("jei", IJeiIntegration.DEFAULT, () -> JeiIntegration.INSTANCE);
    public static final IConfigIntegration CONFIG = mod("cloth", IConfigIntegration.DEFAULT, () -> ClothConfigIntegration.INSTANCE);

    private static IPlatformIntegration loadPlatform() {
        final IPlatformIntegration loadedService = ServiceLoader.load(IPlatformIntegration.class, CoasIntegrations.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load platform integration"));
        LOGGER.info("Loaded platform integration for {}", loadedService.getPlatformName());
        return loadedService;
    }

    private static List<IInventoryExtender> getInventoryExtenders() {
        List<Optional<IInventoryExtender>> result = new ArrayList<>();
        PLATFORM.getInventoryExtenders((a, b) -> result.add(getIntegration(a, b)));
        return result.stream().flatMap(Optional::stream).toList();
    }

    private static <T> T mod(String modid, T fallback, Supplier<? extends T> integration) {
        return CoasIntegrations.<T>getIntegration(modid, integration).orElse(fallback);
    }

    private static <T> Optional<T> getIntegration(String modid, Supplier<? extends T> integration) {
        if (PLATFORM.isModLoaded(modid)) {
            LOGGER.info("Using integration for {}", modid);
            return Optional.of(integration.get());
        }
        LOGGER.info("Skipping integration for {} since it is not present.", modid);
        return Optional.empty();
    }
}
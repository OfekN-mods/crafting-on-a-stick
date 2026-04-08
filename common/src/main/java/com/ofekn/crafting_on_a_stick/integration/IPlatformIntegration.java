package com.ofekn.crafting_on_a_stick.integration;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IPlatformIntegration {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    void sendPacketToServer(CustomPacketPayload payload);

    default void getInventoryExtenders(BiConsumer<String, Supplier<IInventoryExtender>> output) {}

    IConfigIntegration getConfigIntegration();
}
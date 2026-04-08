package com.ofekn.crafting_on_a_stick.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.integration.IConfigIntegration;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public enum GsonConfigIntegration implements IConfigIntegration {
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path COMMON_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("crafting_on_a_stick-common.json");
    private static final Path CLIENT_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("crafting_on_a_stick-client.json");

    private CommonData commonData = new CommonData();
    private ClientData clientData = new ClientData();

    @ApiStatus.Internal
    public static void load() {
        INSTANCE.commonData = loadOrCreate(COMMON_PATH, CommonData.class, new CommonData());
        INSTANCE.clientData = loadOrCreate(CLIENT_PATH, ClientData.class, new ClientData());
    }

    @SuppressWarnings("ConstantValue") // fromJson can return null
    private static <T> T loadOrCreate(Path path, Class<T> clazz, T defaults) {
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                T loaded = GSON.fromJson(reader, clazz);
                if (loaded != null) return loaded;
            } catch (IOException e) {
                LOGGER.error("Failed to read config at '{}' — using defaults.", path);
            }
        }
        // File missing or unreadable: write defaults so the user can edit it
        save(path, defaults);
        return defaults;
    }

    private static void save(Path path, Object data) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config at '{}'", path, e);
        }
    }

    // --- IConfigIntegration ---

    @Override
    public String getWheelType() {
        return clientData.wheelType;
    }

    @Override
    public void setWheelType(String wheelType) {
        clientData.wheelType = wheelType;
        save(CLIENT_PATH, clientData);   // persist immediately, just like NeoForge does
    }

    @Override
    public boolean getStoreItems() {
        return commonData.storeItems;
    }

    private static class CommonData {
        /** Whether to store items in the crafting stations when they are closed */
        boolean storeItems = true;
    }

    private static class ClientData {
        /** The kind of wheel to use, currently "round" or "list" */
        String wheelType = "round";
    }
}

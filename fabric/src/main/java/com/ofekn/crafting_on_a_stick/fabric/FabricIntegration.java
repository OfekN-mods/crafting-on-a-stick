package com.ofekn.crafting_on_a_stick.fabric;

import com.ofekn.crafting_on_a_stick.integration.IPlatformIntegration;
import net.fabricmc.loader.api.FabricLoader;

public class FabricIntegration implements IPlatformIntegration {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}

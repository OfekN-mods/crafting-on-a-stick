package com.ofekn.crafting_on_a_stick.fabric;

import com.ofekn.crafting_on_a_stick.integration.IConfigIntegration;
import com.ofekn.crafting_on_a_stick.integration.IPlatformIntegration;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

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

    @Override
    public void sendPacketToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public IConfigIntegration getConfigIntegration() {
        return GsonConfigIntegration.INSTANCE;
    }
}

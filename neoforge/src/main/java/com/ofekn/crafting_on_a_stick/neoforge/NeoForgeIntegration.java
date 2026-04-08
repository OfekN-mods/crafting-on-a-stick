package com.ofekn.crafting_on_a_stick.neoforge;

import com.ofekn.crafting_on_a_stick.integration.IPlatformIntegration;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoForgeIntegration implements IPlatformIntegration {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public void sendPacketToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
package com.ofekn.crafting_on_a_stick.neoforge;

import com.ofekn.crafting_on_a_stick.integration.IConfigIntegration;
import com.ofekn.crafting_on_a_stick.integration.IInventoryExtender;
import com.ofekn.crafting_on_a_stick.integration.IPlatformIntegration;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

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

    @Override
    public void getInventoryExtenders(BiConsumer<String, Supplier<IInventoryExtender>> output) {
        output.accept("curios", () -> CuriosIntegration.INSTANCE);
    }

    @Override
    public IConfigIntegration getConfigIntegration() {
        return NeoForgeConfigIntegration.INSTANCE;
    }
}
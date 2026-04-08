package com.ofekn.crafting_on_a_stick.fabric.client;

import com.ofekn.crafting_on_a_stick.client.CoasClient;
import com.ofekn.crafting_on_a_stick.client.CoasKeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class CoasFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (KeyMapping keyMapping : CoasKeyMappings.LIST) {
            KeyMappingHelper.registerKeyMapping(keyMapping);
        }

        ClientTickEvents.START_CLIENT_TICK.register(CoasClient::tick);
    }
}

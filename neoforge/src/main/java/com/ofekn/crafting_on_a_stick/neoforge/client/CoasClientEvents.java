package com.ofekn.crafting_on_a_stick.neoforge.client;

import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.client.CoasClient;
import com.ofekn.crafting_on_a_stick.client.CoasKeyMappings;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = Coas.MID, value = Dist.CLIENT)
final class CoasClientEvents {
    private CoasClientEvents() {}

    @SubscribeEvent
    public static void event(RegisterClientPayloadHandlersEvent event) {
    }

    @SubscribeEvent
    public static void event(RegisterKeyMappingsEvent event) {
        CoasKeyMappings.LIST.forEach(event::register);
    }

    @SubscribeEvent
    public static void event(ClientTickEvent.Pre event) {
        CoasClient.tick(Minecraft.getInstance());
    }
}

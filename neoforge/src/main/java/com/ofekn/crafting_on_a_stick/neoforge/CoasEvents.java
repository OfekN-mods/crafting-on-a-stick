package com.ofekn.crafting_on_a_stick.neoforge;

import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.network.SBOpen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Coas.MID)
final class CoasEvents {
    private CoasEvents() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SBOpen.TYPE, SBOpen.CODEC, (packet, ctx) -> packet.handle(ctx.player()));
    }


}

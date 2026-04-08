package com.ofekn.crafting_on_a_stick.neoforge;


import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.Coas;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Coas.MID)
public class CoasNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public CoasNeoForge(IEventBus eventBus) {
        LOGGER.info("Hello NeoForge world!");
        Coas.init();
    }
}
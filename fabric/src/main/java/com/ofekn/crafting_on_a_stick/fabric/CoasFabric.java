package com.ofekn.crafting_on_a_stick.fabric;

import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.Coas;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class CoasFabric implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        Coas.init();
    }
}

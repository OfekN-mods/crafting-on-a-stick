package com.ofekn.crafting_on_a_stick.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

public final class CoasClient {
    private CoasClient() {}

    @ApiStatus.Internal
    public static void tick(Minecraft minecraft) {
        openWheelIfClicked(minecraft);
    }

    private static void openWheelIfClicked(Minecraft minecraft) {
        if (!CoasKeyMappings.OPEN_CURIOS_KEY.consumeClick()) {
            return;
        }
        if (minecraft.gui.screen() != null) {
            return;
        }
        Player player = minecraft.player;
        if (player == null) {
            return;
        }
        CoasWheelScreen.trigger(minecraft, player);
    }
}

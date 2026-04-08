package com.ofekn.crafting_on_a_stick.integration;

import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public enum CuriosIntegration implements ICuriosIntegration {
    INSTANCE;

    @Override
    public void getCuriosInventory(Player player, List<Ref<ItemStack>> result) {
        // TODO implement
    }
}

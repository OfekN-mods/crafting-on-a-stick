package com.ofekn.crafting_on_a_stick.integration;

import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ICuriosIntegration {
    void getCuriosInventory(Player player, List<Ref<ItemStack>> result);

    ICuriosIntegration DEFAULT = new ICuriosIntegration() {
        @Override
        public void getCuriosInventory(Player player, List<Ref<ItemStack>> result) {}
    };
}

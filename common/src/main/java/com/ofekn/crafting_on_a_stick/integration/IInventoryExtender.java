package com.ofekn.crafting_on_a_stick.integration;

import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IInventoryExtender {
    void get(Player player, List<Ref<ItemStack>> result);
}

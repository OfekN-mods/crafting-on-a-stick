package com.ofekn.crafting_on_a_stick;

import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class CoasUtils {
    private CoasUtils() {}

    public static Optional<Ref<ItemStack>> searchInventory(Player player, Item item) {
        return searchInventory(player, item, _ -> true);
    }

    public static Optional<Ref<ItemStack>> searchInventory(Player player, Item item, Predicate<ItemStack> filter) {
        List<Ref<ItemStack>> inventory = getFullInventory(player);
        for (Ref<ItemStack> ref : inventory) {
            ItemStack invStack = ref.get();
            if (!invStack.isEmpty() && invStack.getItem() == item && filter.test(invStack)) {
                return Optional.of(ref);
            }
        }
        return Optional.empty();
    }

    public static List<Ref<ItemStack>> getFullInventory(Player player) {
        //TODO implement
        return null;
    }
}

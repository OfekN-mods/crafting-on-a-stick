package com.ofekn.crafting_on_a_stick.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Interface for items that can appear in the selection wheel.
 * <p>
 * Items implementing this interface will be displayed in the selection wheel when the player
 * opens it. When a player selects an item from the wheel, {@link #onWheelAction(Player, Ref)}
 * is called to perform the item's action.
 * <p>
 * The selection wheel uses {@link #getWheelRepresentative(Player, ItemStack)} to determine
 * which items are considered the same for grouping and comparison purposes.
 *
 * @see Ref
 */
public interface IWheelItem  {
    /**
     * Called when the player selects this item from the selection wheel.
     * <p>
     * This method is invoked on the server side when the player chooses this item from the wheel.
     * The {@code stackRef} parameter allows the item to modify the ItemStack in the player's
     * inventory if needed (e.g., consuming durability, changing NBT data, etc.).
     *
     * @param player the player who selected the item
     * @param stackRef a reference to the ItemStack in the player's inventory that can be modified
     */
    void onWheelAction(Player player, Ref<ItemStack> stackRef);

    /**
     * Returns a representative ItemStack for display and comparison in the selection wheel.
     * <p>
     * This method is used to:
     * <ul>
     *   <li>Determine which items are considered the same for grouping in the wheel</li>
     *   <li>Create a display version of the item</li>
     *   <li>Match the selected item with items in the player's inventory</li>
     * </ul>
     * <p>
     * The default implementation normalizes the stack count to 1 (returns the stack as-is if
     * count is already 1, otherwise creates a copy with count 1). This ensures that items with
     * different counts are treated as the same for grouping purposes.
     * <p>
     * Implementations can override this method to customize the representative stack. For example:
     * <ul>
     *   <li>An anvil implementation might convert all anvil variants (anvil, chipped anvil,
     *       damaged anvil) to a single representative so they all appear as one group in the wheel</li>
     *   <li>An implementation might want to remove certain data components that should not be
     *       considered when comparing items (such as container contents that may differ between instances)</li>
     * </ul>
     *
     * @param player the player whose inventory is being searched
     * @param stack the ItemStack to create a representative for
     * @return a representative ItemStack suitable for display and comparison. Must not be null.
     */
    default ItemStack getWheelRepresentative(Player player, ItemStack stack) {
        return stack.getCount() == 1 ? stack : stack.copyWithCount(1);
    }
}

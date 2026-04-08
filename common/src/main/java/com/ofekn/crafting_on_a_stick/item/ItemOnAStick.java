package com.ofekn.crafting_on_a_stick.item;

import com.ofekn.crafting_on_a_stick.api.IWheelItem;
import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class ItemOnAStick extends Item implements IWheelItem {
    private final MenuProvider menuProvider;
    private final Component itemName;

    public ItemOnAStick(Item.Properties properties, Block block, String containerName, MinecraftMenuBuilder builder) {
        super(properties);
        this.menuProvider = new SimpleMenuProvider(
                (id, inv, player) -> builder.create(id, inv, new DummyContainerLevelAccess(player)),
                Component.translatable("container." + containerName)
        );
        Component blockName = block.getName();
        Component templateName = Component.translatable("item.crafting_on_a_stick.template", blockName);
        this.itemName = Component.translatable(this.descriptionId, templateName);
    }

    private MenuProvider createMenuProviderWrapper(@Nullable ItemContainerContents contents) {
        if (contents == null) {
            return menuProvider;
        }
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return menuProvider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                AbstractContainerMenu result = menuProvider.createMenu(containerId, playerInventory, player);
                if (result == null) {
                    return null;
                }
                var contentsList = contents.allItemsCopyStream().toList();
                for (int i = 0; i < contentsList.size(); i++) {
                    if (result.isValidSlotIndex(i)) {
                        result.setItem(i, 0, contentsList.get(i));
                    }
                }
                return result;
            }
        };
    }

    public ItemStack openContainer(Player player, ItemStack stack) {
        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        stack = stack.copy();
        stack.remove(DataComponents.CONTAINER);
        player.openMenu(createMenuProviderWrapper(contents));
        return stack;

    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(openContainer(player, stack));
    }

    @Override
    public Component getName(ItemStack stack) {
        return itemName;
    }

    @Override
    public void onWheelAction(Player player, Ref<ItemStack> stackRef) {
        stackRef.set(openContainer(player, stackRef.get()));
    }
}
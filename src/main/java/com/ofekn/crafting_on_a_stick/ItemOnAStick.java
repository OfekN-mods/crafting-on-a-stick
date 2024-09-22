package com.ofekn.crafting_on_a_stick;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemOnAStick extends Item {
	private static final Item.Properties PROP = new Item.Properties().stacksTo(1);

	public final String registryPath;
	public final MenuProvider menuProvider;

	public ItemOnAStick(String registryPath, String containerName, MinecraftMenuBuilder builder) {
		super(PROP);
		this.registryPath = registryPath;
		this.menuProvider = new SimpleMenuProvider(
				(id, inv, player) -> builder.create(id, inv, new EntityContainerLevelAccess(player)),
				Component.translatable("container." + containerName)
		);
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
				for (int i = 0; i < contents.getSlots(); i++) {
					if (result.isValidSlotIndex(i)) {
						result.setItem(i, 0, contents.getStackInSlot(i));
					}
				}
				return result;
			}

			@Override
			public boolean shouldTriggerClientSideContainerClosingOnOpen() {
				return menuProvider.shouldTriggerClientSideContainerClosingOnOpen();
			}
		};
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
		stack.remove(DataComponents.CONTAINER);
		player.openMenu(createMenuProviderWrapper(contents));
		return InteractionResultHolder.success(stack);
	}

	@Override
	public Component getName(ItemStack stack) {
		return Component.translatable(
				this.getDescriptionId(stack),
				Component.translatable("gui.crafting_on_a_stick.prefix"),
				Component.translatable("block.minecraft." + registryPath),
				Component.translatable("gui.crafting_on_a_stick.suffix")
		);
	}
}

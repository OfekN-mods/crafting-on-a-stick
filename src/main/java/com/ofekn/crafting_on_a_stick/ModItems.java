package com.ofekn.crafting_on_a_stick;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ModItems {
	private ModItems() {}
	
	public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(CraftingOnAStick.ID);


	private static boolean doPlayerHave(Player player, DeferredItem<ItemOnAStick> itemReg) {
		Item item = itemReg.get();
		
		IItemHandlerModifiable inventory = COASUtils.getFullInventory(player);
		int size = inventory.getSlots();
		
		for (int i = 0; i < size; i++) {
			ItemStack invStack = inventory.getStackInSlot(i);
			if (!invStack.isEmpty() && invStack.getItem() == item)
				return true;
		}

		return false;
	}

	private static void onContainerClosed(Player player, DeferredItem<ItemOnAStick> itemReg, AbstractContainerMenu menu, int offset, int slotCount) {
		if (!COASConfig.getStoreItems()) {
			return;
		}
		Item item = itemReg.get();

		IItemHandlerModifiable inventory = COASUtils.getFullInventory(player);
		int size = inventory.getSlots();

		for (int i = 0; i < size; i++) {
			ItemStack invStack = inventory.getStackInSlot(i);
			if (invStack.isEmpty() || invStack.getItem() != item || invStack.has(DataComponents.CONTAINER)) {
				continue;
			}
			ItemContainerContents result = getWorkbenchContent(menu, offset, slotCount);
			invStack.set(DataComponents.CONTAINER, result);
			return;
		}
	}

	@Nullable
	private static ItemContainerContents getWorkbenchContent(AbstractContainerMenu menu, int offset, int slotCount) {
		List<ItemStack> result = new ArrayList<>();
		for (int i = 0; i < offset; i++) {
			result.add(ItemStack.EMPTY);
		}
		boolean hasItems = false;
		for (int i = 0; i < slotCount; i++) {
			ItemStack stack = menu.getSlot(offset + i).getItem();
			result.add(stack);
			if (!stack.isEmpty()) {
				hasItems = true;
			}
		}
		for (int i = 0; i < slotCount; i++) {
			menu.getSlot(i + offset).set(ItemStack.EMPTY);
		}
		if (!hasItems) {
			return null;
		}
		return ItemContainerContents.fromItems(result);
	}


	public static final DeferredItem<ItemOnAStick> CRAFTING_TABLE = createItem(Blocks.CRAFTING_TABLE, "crafting",
			(a,b,c)->new CraftingMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, CRAFTING_TABLE);
				}

				@Override
				public void removed(Player player) {
					onContainerClosed(player, CRAFTING_TABLE, this, 1, 9);
					super.removed(player);
				}
			});

	public static final DeferredItem<ItemOnAStick> LOOM = createItem(
			Blocks.LOOM,
			"loom",
			(a,b,c)->new LoomMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, LOOM);
				}
			});
	public static final DeferredItem<ItemOnAStick> GRINDSTONE = createItem(
			Blocks.GRINDSTONE,
			"grindstone_title",
			(a,b,c)->new GrindstoneMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, GRINDSTONE);
				}
			});
	public static final DeferredItem<ItemOnAStick> CARTOGRAPHY_TABLE = createItem(
			Blocks.CARTOGRAPHY_TABLE,
			"cartography_table",
			(a,b,c)->new CartographyTableMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, CARTOGRAPHY_TABLE);
				}
			});
	public static final DeferredItem<ItemOnAStick> STONECUTTER = createItem(
			Blocks.STONECUTTER,
			"stonecutter",
			(a,b,c)->new StonecutterMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, STONECUTTER);
				}
			});
	public static final DeferredItem<ItemOnAStick> SMITHING_TABLE = createItem(
			Blocks.SMITHING_TABLE,
			"upgrade",
			(a,b,c)->new SmithingMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, SMITHING_TABLE);
				}
			});
	public static final DeferredItem<ItemOnAStick> ANVIL         = createAnvil(Blocks.ANVIL);
	public static final DeferredItem<ItemOnAStick> CHIPPED_ANVIL = createAnvil(Blocks.CHIPPED_ANVIL);
	public static final DeferredItem<ItemOnAStick> DAMAGED_ANVIL = createAnvil(Blocks.DAMAGED_ANVIL);













	private static DeferredItem<ItemOnAStick> createItem(Block block, String containerName, MinecraftMenuBuilder builder) {
		ResourceKey<Block> blockKey = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
		String path = blockKey.location().getPath();
		return REGISTER.register(path, ()->new ItemOnAStick(path, containerName, builder));
	}

	private static DeferredItem<ItemOnAStick> createAnvil(Block block) {
		return createItem(block, "repair", (a,b,c)->new AnvilMenu(a,b,c) {
			@Override
			public boolean stillValid(Player player) {
				return  doPlayerHave(player, DAMAGED_ANVIL) ||
						doPlayerHave(player, CHIPPED_ANVIL) ||
						doPlayerHave(player, ANVIL);
			}

			@Override
			protected void onTake(Player p_150474_, ItemStack p_150475_) {
				if (!p_150474_.getAbilities().instabuild) {
					p_150474_.giveExperienceLevels(-this.getCost());
				}

				float breakChance = CommonHooks.onAnvilRepair(p_150474_, p_150475_, this.inputSlots.getItem(0), this.inputSlots.getItem(1));

				this.inputSlots.setItem(0, ItemStack.EMPTY);
				if (this.repairItemCountCost > 0) {
					ItemStack itemstack = this.inputSlots.getItem(1);
					if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
						itemstack.shrink(this.repairItemCountCost);
						this.inputSlots.setItem(1, itemstack);
					} else {
						this.inputSlots.setItem(1, ItemStack.EMPTY);
					}
				} else {
					this.inputSlots.setItem(1, ItemStack.EMPTY);
				}

				this.setMaximumCost(0);
				this.access.execute((p_150479_, p_150480_) ->
						p_150479_.levelEvent(damageAnvil(p_150474_, breakChance) ? 1029 : 1030, p_150480_, 0));
			}
		});
	}











	private static boolean damageAnvil(Player player, float breakChance) {
		if (player.getAbilities().instabuild || player.getRandom().nextFloat() >= breakChance)
			return false;

		ItemStack candidate;

		candidate = damageAnvilItemStack(player.getItemInHand(InteractionHand.MAIN_HAND));
		if (candidate != null) {
			player.setItemInHand(InteractionHand.MAIN_HAND, candidate);
			return candidate.isEmpty();
		}

		candidate = damageAnvilItemStack(player.getItemInHand(InteractionHand.OFF_HAND));
		if (candidate != null) {
			player.setItemInHand(InteractionHand.OFF_HAND, candidate);
			return candidate.isEmpty();
		}
		
		IItemHandlerModifiable inventory = COASUtils.getFullInventory(player);
		int invSize = inventory.getSlots();
		for (int i = 0; i < invSize; i++) {
			candidate = damageAnvilItemStack(inventory.getStackInSlot(i));
			if (candidate != null) {
				inventory.setStackInSlot(i, candidate);
				return candidate.isEmpty();
			}
		}

		return false;
	}

	@Nullable
	private static ItemStack damageAnvilItemStack(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		Item item = stack.getItem();
		if (item == ANVIL.get())
			return new ItemStack(CHIPPED_ANVIL.get());
		if (item == CHIPPED_ANVIL.get())
			return new ItemStack(DAMAGED_ANVIL.get());
		if (item == DAMAGED_ANVIL.get())
			return ItemStack.EMPTY;
		return null;
	}
}

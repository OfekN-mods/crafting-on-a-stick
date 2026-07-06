package com.ofekn.crafting_on_a_stick;

import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ModItems {
	private ModItems() {}
    private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(CraftingOnAStick.ID);


    private static Optional<Ref<ItemStack>> searchInventory(Player player, DeferredItem<ItemOnAStick> itemReg, Predicate<ItemStack> filter) {
        Item item = itemReg.get();
        List<Ref<ItemStack>> inventory = COASUtils.getFullInventory(player);
        for (Ref<ItemStack> ref : inventory) {
            ItemStack invStack = ref.get();
            if (!invStack.isEmpty() && invStack.getItem() == item && filter.test(invStack)) {
                return Optional.of(ref);
            }
        }
        return Optional.empty();
    }

	private static boolean doPlayerHave(Player player, DeferredItem<ItemOnAStick> itemReg) {
        return searchInventory(player, itemReg, stack -> true).isPresent();
	}

	private static void onContainerClosed(Player player, DeferredItem<ItemOnAStick> itemReg, AbstractContainerMenu menu, int offset, int slotCount) {
		if (!COASConfig.getStoreItems()) {
			return;
		}
//		if (player.isRemoved()) {
//			// when the player leaves the world
//			// it saves, and then closes the container
//			// updating the player inventory won't save
//			// so we prefer dropping the items
//			return;
//		}

        searchInventory(player, itemReg, stack -> !stack.has(DataComponents.CONTAINER)).ifPresent(ref -> {
            ItemStack stack = ref.get();
            ItemContainerContents result = getWorkbenchContent(menu, offset, slotCount);
            stack.set(DataComponents.CONTAINER, result);
            ref.set(stack);
        });
	}

	private static boolean shouldKeepItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		return stack.getItem().canFitInsideContainerItems();
	}

	@Nullable
	private static ItemContainerContents getWorkbenchContent(AbstractContainerMenu menu, int offset, int slotCount) {
		List<ItemStack> result = new ArrayList<>();
		for (int i = 0; i < offset; i++) {
			result.add(ItemStack.EMPTY);
		}
		boolean hasItems = false;
		for (int i = 0; i < slotCount; i++) {
			Slot slot = menu.getSlot(offset + i);
			ItemStack stack = slot.getItem();
			if (!shouldKeepItem(stack)) {
				result.add(ItemStack.EMPTY);
				continue;
			}
			hasItems = true;
			result.add(stack);
			slot.set(ItemStack.EMPTY);
		}
		if (!hasItems) {
			return null;
		}
		return ItemContainerContents.fromItems(result);
	}


	public static final DeferredItem<ItemOnAStick> CRAFTING_TABLE = createSimpleItem(Blocks.CRAFTING_TABLE, "crafting",
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

	public static final DeferredItem<ItemOnAStick> LOOM = createSimpleItem(
			Blocks.LOOM,
			"loom",
			(a,b,c)->new LoomMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, LOOM);
				}
			});
	public static final DeferredItem<ItemOnAStick> GRINDSTONE = createSimpleItem(
			Blocks.GRINDSTONE,
			"grindstone_title",
			(a,b,c)->new GrindstoneMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, GRINDSTONE);
				}
			});
	public static final DeferredItem<ItemOnAStick> CARTOGRAPHY_TABLE = createSimpleItem(
			Blocks.CARTOGRAPHY_TABLE,
			"cartography_table",
			(a,b,c)->new CartographyTableMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, CARTOGRAPHY_TABLE);
				}
			});
	public static final DeferredItem<ItemOnAStick> STONECUTTER = createSimpleItem(
			Blocks.STONECUTTER,
			"stonecutter",
			(a,b,c)->new StonecutterMenu(a,b,c) {
				@Override
				public boolean stillValid(Player player) {
					return doPlayerHave(player, STONECUTTER);
				}
			});
	public static final DeferredItem<ItemOnAStick> SMITHING_TABLE = createSimpleItem(
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













	private static DeferredItem<ItemOnAStick> createSimpleItem(Block block, String containerName, MinecraftMenuBuilder builder) {
        return createItem(block, (path) -> new ItemOnAStick(path, containerName, builder));
	}

    private static <T extends Item> DeferredItem<T> createItem(Block block, Function<String, T> itemConstructor) {
        ResourceKey<Block> blockKey = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
        String path = blockKey.location().getPath();
        return REGISTER.register(path, ()->itemConstructor.apply(path));
    }

	private static DeferredItem<ItemOnAStick> createAnvil(Block block) {
        MinecraftMenuBuilder builder = (a, b, c)->new AnvilMenu(a,b,c) {
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
        };
		return createItem(block, (path) -> new ItemOnAStick(path, "repair", builder) {
            @Override
            public ItemStack getWheelRepresentative(Player player, ItemStack stack) {
                return stack.transmuteCopy(ANVIL, 1);
            }
        });
	}











	private static boolean damageAnvil(Player player, float breakChance) {
		if (player.getAbilities().instabuild || player.getRandom().nextFloat() >= breakChance) {
            return false;
        }

        Optional<Ref<ItemStack>> anvilRef;

        anvilRef = searchInventory(player, DAMAGED_ANVIL, stack -> true);
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ItemStack.EMPTY);
            return true;
        }

        anvilRef = searchInventory(player, CHIPPED_ANVIL, stack -> true);
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ref.get().transmuteCopy(DAMAGED_ANVIL));
            return false;
        }

        anvilRef = searchInventory(player, ANVIL, stack -> true);
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ref.get().transmuteCopy(CHIPPED_ANVIL));
            return false;
        }

        LOGGER.warn("failed to get anvil to damage for {}", player);

		return false;
	}
}

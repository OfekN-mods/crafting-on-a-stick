package com.ofekn.crafting_on_a_stick.item;

import com.mojang.logging.LogUtils;
import com.ofekn.crafting_on_a_stick.CoasUtils;
import com.ofekn.crafting_on_a_stick.api.Ref;
import com.ofekn.crafting_on_a_stick.integration.CoasIntegrations;
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
import net.minecraft.world.level.block.LevelEvent;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class CoasItem<I extends Item> implements Supplier<I> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<CoasItem<?>> ITEMS = new ArrayList<>();
    private final String name;
    private final Function<Item.Properties, I> constructor;
    private final Function<Item.Properties, Item.Properties> properties;
    @Nullable
    private Supplier<I> value;

    private CoasItem(String name, Function<Item.Properties, I> constructor, Function<Item.Properties, Item.Properties> properties) {
        this.name = name;
        this.constructor = constructor;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Function<Item.Properties, I> getConstructor() {
        return constructor;
    }

    public Function<Item.Properties, Item.Properties> getProperties() {
        return properties;
    }

    public void bind(Supplier<I> value) {
        if (this.value != null) {
            throw new IllegalStateException("called bind twice");
        }
        this.value = value;
    }

    @Override
    public I get() {
        if (value == null) {
            throw new IllegalStateException("called get on an unbound item");
        }
        return value.get();
    }

    public static List<CoasItem<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    private static boolean doPlayerHave(Player player, Supplier<? extends Item> itemReg) {
        return CoasUtils.searchInventory(player, itemReg.get()).isPresent();
    }

    private static void onContainerClosed(Player player, Supplier<? extends Item> itemReg, AbstractContainerMenu menu, int offset, int slotCount) {
        if (!CoasIntegrations.CONFIG.getStoreItems()) {
            return;
        }

        CoasUtils.searchInventory(player, itemReg.get(), stack -> !stack.has(DataComponents.CONTAINER)).ifPresent(ref -> {
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


    public static final CoasItem<ItemOnAStick> CRAFTING_TABLE = createSimpleItem(Blocks.CRAFTING_TABLE, "crafting",
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

    public static final CoasItem<ItemOnAStick> LOOM = createSimpleItem(
            Blocks.LOOM,
            "loom",
            (a,b,c)->new LoomMenu(a,b,c) {
                @Override
                public boolean stillValid(Player player) {
                    return doPlayerHave(player, LOOM);
                }
            });
    public static final CoasItem<ItemOnAStick> GRINDSTONE = createSimpleItem(
            Blocks.GRINDSTONE,
            "grindstone_title",
            (a,b,c)->new GrindstoneMenu(a,b,c) {
                @Override
                public boolean stillValid(Player player) {
                    return doPlayerHave(player, GRINDSTONE);
                }
            });
    public static final CoasItem<ItemOnAStick> CARTOGRAPHY_TABLE = createSimpleItem(
            Blocks.CARTOGRAPHY_TABLE,
            "cartography_table",
            (a,b,c)->new CartographyTableMenu(a,b,c) {
                @Override
                public boolean stillValid(Player player) {
                    return doPlayerHave(player, CARTOGRAPHY_TABLE);
                }
            });
    public static final CoasItem<ItemOnAStick> STONECUTTER = createSimpleItem(
            Blocks.STONECUTTER,
            "stonecutter",
            (a,b,c)->new StonecutterMenu(a,b,c) {
                @Override
                public boolean stillValid(Player player) {
                    return doPlayerHave(player, STONECUTTER);
                }
            });
    public static final CoasItem<ItemOnAStick> SMITHING_TABLE = createSimpleItem(
            Blocks.SMITHING_TABLE,
            "upgrade",
            (a,b,c)->new SmithingMenu(a,b,c) {
                @Override
                public boolean stillValid(Player player) {
                    return doPlayerHave(player, SMITHING_TABLE);
                }
            });
    public static final CoasItem<ItemOnAStick> ANVIL         = createAnvil(Blocks.ANVIL);
    public static final CoasItem<ItemOnAStick> CHIPPED_ANVIL = createAnvil(Blocks.CHIPPED_ANVIL);
    public static final CoasItem<ItemOnAStick> DAMAGED_ANVIL = createAnvil(Blocks.DAMAGED_ANVIL);













    private static CoasItem<ItemOnAStick> createSimpleItem(Block block, String containerName, MinecraftMenuBuilder builder) {
        return createItem(block, (props) -> new ItemOnAStick(props, block, containerName, builder), CoasItem::itemOnAStickProperties);
    }

    private static Item.Properties itemOnAStickProperties(Item.Properties properties) {
        return properties.stacksTo(1);
    }

    private static <T extends Item> CoasItem<T> createItem(Block block, Function<Item.Properties, T> itemConstructor, Function<Item.Properties, Item.Properties> properties) {
        ResourceKey<Block> blockKey = BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
        String path = blockKey.identifier().getPath();
        var newItem = new CoasItem<>(path, itemConstructor, properties);
        ITEMS.add(newItem);
        return newItem;
    }

    private static CoasItem<ItemOnAStick> createAnvil(Block block) {
        MinecraftMenuBuilder builder = (a, b, c)->new AnvilMenu(a,b,c) {
            @Override
            public boolean stillValid(Player player) {
                return  doPlayerHave(player, DAMAGED_ANVIL) ||
                        doPlayerHave(player, CHIPPED_ANVIL) ||
                        doPlayerHave(player, ANVIL);
            }

            @Override
            protected void onTake(Player player, ItemStack carried) {
                if (this.access instanceof DummyContainerLevelAccess dummy) {
                    dummy.setDummySupplier((level, pos) -> {
                        level.levelEvent(damageAnvil(player, 0.12f) ? LevelEvent.SOUND_ANVIL_BROKEN : LevelEvent.SOUND_ANVIL_USED, pos, 0);
                        return null;
                    });
                } else {
                    super.onTake(player, carried);
                }
            }
        };
        return createItem(block, (props) -> new ItemOnAStick(props, block, "repair", builder) {
            @Override
            public ItemStack getWheelRepresentative(Player player, ItemStack stack) {
                return stack.transmuteCopy(ANVIL.get(), 1);
            }
        }, CoasItem::itemOnAStickProperties);
    }











    private static boolean damageAnvil(Player player, float breakChance) {
        if (player.getAbilities().instabuild || player.getRandom().nextFloat() >= breakChance) {
            return false;
        }

        Optional<Ref<ItemStack>> anvilRef;

        anvilRef = CoasUtils.searchInventory(player, DAMAGED_ANVIL.get());
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ItemStack.EMPTY);
            return true;
        }

        anvilRef = CoasUtils.searchInventory(player, CHIPPED_ANVIL.get());
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ref.get().transmuteCopy(DAMAGED_ANVIL.get()));
            return false;
        }

        anvilRef = CoasUtils.searchInventory(player, ANVIL.get());
        if (anvilRef.isPresent()) {
            Ref<ItemStack> ref = anvilRef.get();
            ref.set(ref.get().transmuteCopy(CHIPPED_ANVIL.get()));
            return false;
        }

        LOGGER.warn("failed to get anvil to damage for {}", player);

        return false;
    }
}

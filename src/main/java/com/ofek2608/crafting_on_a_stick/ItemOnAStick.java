package com.ofek2608.crafting_on_a_stick;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.openMenu(menuProvider);
		return InteractionResultHolder.success(player.getItemInHand(hand));
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

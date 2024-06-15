package com.ofekn.crafting_on_a_stick;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public interface MinecraftMenuBuilder {
	AbstractContainerMenu create(int id, Inventory inventory, ContainerLevelAccess access);
}

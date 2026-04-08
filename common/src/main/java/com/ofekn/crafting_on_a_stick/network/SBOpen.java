package com.ofekn.crafting_on_a_stick.network;

import com.ofekn.crafting_on_a_stick.Coas;
import com.ofekn.crafting_on_a_stick.CoasUtils;
import com.ofekn.crafting_on_a_stick.api.IWheelItem;
import com.ofekn.crafting_on_a_stick.api.Ref;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record SBOpen(ItemStack selected) implements CustomPacketPayload {
	public static final Type<SBOpen> TYPE = new Type<>(Coas.id("sb_open"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SBOpen> CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC, SBOpen::selected,
			SBOpen::new
	);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

	public void handle(Player player) {
        List<Ref<ItemStack>> inventory = CoasUtils.getFullInventory(player);
        for (Ref<ItemStack> ref : inventory) {
            ItemStack inventoryStack = ref.get();
            if (inventoryStack.isEmpty()) {
                continue;
            }
            if (!(inventoryStack.getItem() instanceof IWheelItem wheelItem)) {
                continue;
            }
            ItemStack representative = wheelItem.getWheelRepresentative(player, inventoryStack);
            if (!ItemStack.isSameItemSameComponents(representative, selected)) {
                continue;
            }
            wheelItem.onWheelAction(player, ref);
            break;
        }
	}
}

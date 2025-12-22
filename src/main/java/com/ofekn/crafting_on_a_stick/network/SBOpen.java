package com.ofekn.crafting_on_a_stick.network;

import com.ofekn.crafting_on_a_stick.COASUtils;
import com.ofekn.crafting_on_a_stick.ItemOnAStick;
import com.ofekn.crafting_on_a_stick.Ref;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

import static com.ofekn.crafting_on_a_stick.CraftingOnAStick.modLoc;

public record SBOpen(ItemStack selected) implements CustomPacketPayload {
	public static final Type<SBOpen> TYPE = new Type<>(modLoc("sb_open"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SBOpen> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, SBOpen::selected,
            SBOpen::new
    );

	void handle(IPayloadContext context) {
		context.enqueueWork(()->{
			Player player = context.player();
			if (!(player instanceof ServerPlayer)) {
                return;
            }
            List<Ref<ItemStack>> inventory = COASUtils.getFullInventory(player);
            for (Ref<ItemStack> ref : inventory) {
                ItemStack inventoryStack = ref.get();
                if (inventoryStack.isEmpty()) {
                    continue;
                }
                if (!ItemStack.isSameItemSameComponents(inventoryStack, selected)) {
                    continue;
                }
                if (!(inventoryStack.getItem() instanceof ItemOnAStick)) {
                    continue;
                }
                ref.set(ItemOnAStick.openContainer(player, inventoryStack));
                break;
            }
		});
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

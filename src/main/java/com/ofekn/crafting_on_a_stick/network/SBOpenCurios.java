package com.ofekn.crafting_on_a_stick.network;

import com.ofekn.crafting_on_a_stick.ItemOnAStick;
import com.ofekn.crafting_on_a_stick.integration.COASCurios;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.ofekn.crafting_on_a_stick.CraftingOnAStick.modLoc;

public final class SBOpenCurios implements CustomPacketPayload {
	public static final SBOpenCurios INSTANCE = new SBOpenCurios();
	public static final Type<SBOpenCurios> TYPE = new Type<>(modLoc("sb_open_curios"));
	public static final StreamCodec<FriendlyByteBuf,SBOpenCurios> CODEC = StreamCodec.unit(INSTANCE);

	private SBOpenCurios() {}

	void handle(IPayloadContext context) {
		
		context.enqueueWork(()->{
			Player player = context.player();
			if (!(player instanceof ServerPlayer))
				return;
			Optional<IItemHandlerModifiable> curiosInvOpt = COASCurios.getCuriosInventory(player);
			if (curiosInvOpt.isEmpty())
				return;
			IItemHandlerModifiable curiosInv = curiosInvOpt.get();
			int size = curiosInv.getSlots();
			for (int i = 0; i < size; i++) {
				ItemStack stack = curiosInv.getStackInSlot(i);
				if (stack.getItem() instanceof ItemOnAStick) {
					stack = ItemOnAStick.openContainer(player, stack);
					curiosInv.setStackInSlot(i, stack);
					break;
				}
			}
		});
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

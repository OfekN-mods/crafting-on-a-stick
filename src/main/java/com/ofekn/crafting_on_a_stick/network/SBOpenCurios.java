package com.ofekn.crafting_on_a_stick.network;

//import com.ofek2608.crafting_on_a_stick.ItemOnAStick;
//import com.ofek2608.crafting_on_a_stick.integration.COASCurios;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.Optional;
//import java.util.UUID;
//import java.util.function.Supplier;
//
//class SBOpenCurios {
//
//	SBOpenCurios() {}
//
//	SBOpenCurios(FriendlyByteBuf buf) {
//		this();
//	}
//
//	@SuppressWarnings("EmptyMethod")
//	void encode(FriendlyByteBuf buf) {
//	}
//
//	void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
//		ctxSupplier.get().enqueueWork(()->{
//			ServerPlayer player = ctxSupplier.get().getSender();
//			if (player == null)
//				return;
//			Optional<IItemHandlerModifiable> curiosInvOpt = COASCurios.getCuriosInventory(player);
//			if (curiosInvOpt.isEmpty())
//				return;
//			IItemHandlerModifiable curiosInv = curiosInvOpt.get();
//			int size = curiosInv.getSlots();
//			for (int i = 0; i < size; i++) {
//				ItemStack stack = curiosInv.getStackInSlot(i);
//				if (!stack.isEmpty() && stack.getItem() instanceof ItemOnAStick)
//					stack.use(player.level(), player, InteractionHand.MAIN_HAND);
//			}
//		});
//		ctxSupplier.get().setPacketHandled(true);
//	}
//}

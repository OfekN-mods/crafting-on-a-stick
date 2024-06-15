package com.ofekn.crafting_on_a_stick.network;

//import com.ofek2608.crafting_on_a_stick.CraftingOnAStick;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.NetworkRegistry;
//import net.minecraftforge.network.PacketDistributor;
//import net.minecraftforge.network.simple.SimpleChannel;
//
//import java.util.Optional;
//
//public final class COASPacketHandler {
//	private COASPacketHandler() {}
//	public static void loadClass() {}
//
//	private static final String PROTOCOL_VERSION = "1.0.0";
//	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(CraftingOnAStick.loc("main"), ()->PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
//
//	static {
//		int pid = -1;
////		Optional<NetworkDirection> clientbound = Optional.of(NetworkDirection.PLAY_TO_CLIENT);
//		Optional<NetworkDirection> serverbound = Optional.of(NetworkDirection.PLAY_TO_SERVER);
//
//		CHANNEL.registerMessage(++pid, SBOpenCurios.class, SBOpenCurios::encode, SBOpenCurios::new, SBOpenCurios::handle, serverbound);
//	}
//
//	private static PacketDistributor.PacketTarget serverTarget() { return PacketDistributor.SERVER.noArg(); }
//	public static void sbOpenCurios() { CHANNEL.send(serverTarget(), new SBOpenCurios()); }
//}

package com.tom.cpm.mixin.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.class_454;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.Connection;
import net.minecraft.network.Packet;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import com.tom.cpm.client.ClientNetworkImpl;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin implements ClientNetworkImpl {
	private @Shadow @Final Minecraft minecraft;
	private @Shadow Connection connection;
	private @Shadow class_454 field_1973;
	private boolean cpm$hasMod;

	public @Shadow abstract void sendPacket(Packet packet);

	@Override
	public boolean cpm$hasMod() {
		return cpm$hasMod;
	}

	@Override
	public void cpm$setHasMod(boolean v) {
		cpm$hasMod = v;
	}

	@Override
	public void cpm$sendPacket(Identifier id, byte[] data) {
		MessagePacket p = new MessagePacket(id);
		p.bytes = data;
		sendPacket(p);
	}

	@Override
	public Entity cpm$getEntityByID(int id) {
		if (minecraft.player.id == id)return minecraft.player;
		return field_1973.method_1496(id);
	}

	@Override
	public String cpm$getConnectedServer() {
		SocketAddress sa = connection.getAddress();
		if (sa instanceof InetSocketAddress)
			return ((InetSocketAddress) sa).getHostString();
		return null;
	}
}

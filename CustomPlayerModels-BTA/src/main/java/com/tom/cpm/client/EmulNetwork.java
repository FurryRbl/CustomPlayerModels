package com.tom.cpm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;

import com.tom.cpm.common.ServerHandler;
import com.tom.cpm.common.ServerNetworkImpl;
import com.tom.cpm.shared.config.PlayerData;
import com.tom.cpm.shared.io.FastByteArrayInputStream;

public class EmulNetwork {
	public static final ClientNetworkImpl emulClient = new EmulClient();
	public static final ServerNetworkImpl emulServer = new EmulServer();

	public static ClientNetworkImpl getClient(net.minecraft.core.entity.player.Player pl) {
		if (pl instanceof PlayerLocalMultiplayer) {
			return (ClientNetworkImpl) ((PlayerLocalMultiplayer) pl).sendQueue;
		}
		return EmulNetwork.emulClient;
	}

	public static void reset() {
		emulServer.cpm$setEncodedModelData(null);
		emulServer.cpm$setHasMod(false);
		emulClient.cpm$setHasMod(false);
	}

	private static class EmulClient extends EmulNetwork implements ClientNetworkImpl {
		private boolean hasMod;

		@Override
		public boolean cpm$hasMod() {
			return hasMod;
		}

		@Override
		public void cpm$setHasMod(boolean v) {
			hasMod = v;
		}

		@Override
		public void cpm$sendPacket(String id, byte[] data) {
			ServerHandler.netHandler.receiveServer(id, new FastByteArrayInputStream(data), emulServer);
		}

		@Override
		public Entity cpm$getEntityByID(int id) {
			return Minecraft.INSTANCE.thePlayer;
		}

		@Override
		public String cpm$getConnectedServer() {
			return null;
		}
	}

	private static class EmulServer extends EmulNetwork implements ServerNetworkImpl {
		private boolean hasMod;
		private PlayerData pd;

		@Override
		public boolean cpm$hasMod() {
			return hasMod;
		}

		@Override
		public void cpm$setHasMod(boolean v) {
			hasMod = v;
		}

		@Override
		public void cpm$sendPacket(String id, byte[] data) {
			CustomPlayerModelsClient.netHandler.receiveClient(id, new FastByteArrayInputStream(data), emulClient);
		}

		@Override
		public PlayerData cpm$getEncodedModelData() {
			return pd;
		}

		@Override
		public void cpm$setEncodedModelData(PlayerData data) {
			this.pd = data;
		}

		@Override
		public void cpm$sendChat(String msg) {
			Minecraft.INSTANCE.hudIngame.addChatMessage(msg);
		}

		@Override
		public Player cpm$getPlayer() {
			return Minecraft.INSTANCE.thePlayer;
		}

		@Override
		public void cpm$kickPlayer(String msg) {
		}
	}
}

package com.tom.cpm.client;

import java.io.File;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

import com.tom.cpl.block.BiomeHandler;
import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.IGui;
import com.tom.cpl.gui.IKeybind;
import com.tom.cpl.render.RenderTypeBuilder;
import com.tom.cpl.tag.AllTagManagers;
import com.tom.cpl.util.AWTImageIO;
import com.tom.cpl.util.DynamicTexture.ITexture;
import com.tom.cpl.util.Image;
import com.tom.cpl.util.ImageIO.IImageIO;
import com.tom.cpm.common.BiomeHandlerImpl;
import com.tom.cpm.retro.GameProfile;
import com.tom.cpm.retro.GameProfileManager;
import com.tom.cpm.retro.MCExecutor;
import com.tom.cpm.shared.MinecraftClientAccess;
import com.tom.cpm.shared.MinecraftObjectHolder;
import com.tom.cpm.shared.definition.ModelDefinitionLoader;
import com.tom.cpm.shared.model.SkinType;
import com.tom.cpm.shared.network.NetHandler;
import com.tom.cpm.shared.retro.RetroGLAccess.RetroLayer;
import com.tom.cpm.shared.util.Log;
import com.tom.cpm.shared.util.MojangAPI;

public class MinecraftObject implements MinecraftClientAccess {
	private final Minecraft mc;
	private final PlayerRenderManager prm;
	private final AllTagManagers tags;
	private final ModelDefinitionLoader<GameProfile> loader;
	private final RenderTypeBuilder<Integer, RetroLayer> renderBuilder = RenderTypeBuilder.setupRetro(new RetroGL());

	public MinecraftObject(Minecraft mc) {
		this.mc = mc;
		MinecraftObjectHolder.setClientObject(this);
		loader = new ModelDefinitionLoader<>(PlayerProfile::new, GameProfile::getId, GameProfile::getName);
		prm = new PlayerRenderManager();
		tags = new AllTagManagers(null, CPMTagLoader::new);
	}

	@Override
	public PlayerRenderManager getPlayerRenderManager() {
		return prm;
	}

	@Override
	public ITexture createTexture() {
		return new Texture();
	}

	public static class Texture implements ITexture {
		public static Texture bound;
		private int id = -1;

		public Texture() {
			id = GL11.glGenTextures();
		}

		@Override
		public void bind() {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			bound = this;
		}

		@Override
		public void load(Image image) {
			if (id == -1)id = GL11.glGenTextures();
			Minecraft.INSTANCE.textureManager.method_1089(AWTImageIO.toBufferedImage(image), id);
		}

		@Override
		public void free() {
			Minecraft.INSTANCE.textureManager.delete(id);
			id = -1;
		}

		public int getId() {
			return id;
		}
	}

	@Override
	public void executeOnGameThread(Runnable r) {
		MCExecutor.addScheduledTask(r);
	}

	@Override
	public void executeNextFrame(Runnable r) {
		MCExecutor.tell(r);
	}

	@Override
	public ModelDefinitionLoader<GameProfile> getDefinitionLoader() {
		return loader;
	}

	@Override
	public SkinType getSkinType() {
		return SkinType.DEFAULT;
	}

	@Override
	public void setEncodedGesture(int value) {
		CustomPlayerModelsClient.netHandler.sendLayer(value);
	}

	@Override
	public boolean isInGame() {
		return mc.player != null;
	}

	@Override
	public Object getPlayerIDObject() {
		return GameProfileManager.getProfile(mc.session.username);
	}

	@Override
	public Object getCurrentPlayerIDObject() {
		return mc.player != null ? GameProfileManager.getProfile(mc.player.name) : null;
	}

	@Override
	public List<IKeybind> getKeybinds() {
		return KeyBindings.kbs;
	}

	@Override
	public ServerStatus getServerSideStatus() {
		return isInGame() ? getNetHandler().hasModClient() ? ServerStatus.INSTALLED : ServerStatus.UNAVAILABLE : ServerStatus.OFFLINE;
	}

	@Override
	public File getGameDir() {
		return Minecraft.getRunDirectory();
	}

	@Override
	public void openGui(Function<IGui, Frame> creator) {
		mc.setScreen(new GuiImpl(creator, mc.currentScreen));
	}

	@Override
	public Runnable openSingleplayer() {
		return () -> mc.setScreen(new SelectWorldScreen(mc.currentScreen));
	}

	@Override
	public NetHandler<?, ?, ?> getNetHandler() {
		return CustomPlayerModelsClient.netHandler;
	}

	@Override
	public IImageIO getImageIO() {
		return new AWTImageIO();
	}

	@Override
	public MojangAPI getMojangAPI() {
		GameProfile gp = GameProfileManager.getProfile(mc.session.username);
		return new MojangAPI(gp.getName(), gp.getId(), mc.session.sessionId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void clearSkinCache() {
		GameProfileManager.clear();

		try {
			// Clear Ears cache
			Class<?> earsLegacy = Class.forName("com.unascribed.ears.legacy.LegacyHelper");
			Field f = earsLegacy.getDeclaredField("skinUrls");
			f.setAccessible(true);
			((Map) f.get(null)).clear();
			f = earsLegacy.getDeclaredField("cache");
			f.setAccessible(true);
			((Map) f.get(null)).clear();
			Log.info("Cleared Ears Cache");
		} catch (Throwable e) {
		}
	}

	@Override
	public String getConnectedServer() {
		if (mc.player == null)return null;
		return EmulNetwork.getClient(mc.player).cpm$getConnectedServer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getPlayers() {
		if(mc.player == null)return Collections.emptyList();
		return Collections.emptyList();
		/*
		NetClientHandler net = this.mc.thePlayer.sendQueue;
		List<GuiPlayerInfo> plInfo = net.playerInfoList;
		List<EntityPlayer> players = mc.theWorld.playerEntities;
		return plInfo.stream().map(i -> players.stream().filter(e -> e.getCommandSenderName().equalsIgnoreCase(i.name)).
				findFirst().orElse(null)).filter(e -> e != null).map(e -> GameProfileManager.getProfile(e.username)).collect(Collectors.toList());
		 */
	}

	@Override
	public Proxy getProxy() {
		return Proxy.NO_PROXY;
	}

	@Override
	public RenderTypeBuilder<?, ?> getRenderBuilder() {
		return renderBuilder;
	}

	@Override
	public AllTagManagers getBuiltinTags() {
		return tags;
	}

	@Override
	public BiomeHandler<?> getBiomeHandler() {
		return BiomeHandlerImpl.impl;
	}
}

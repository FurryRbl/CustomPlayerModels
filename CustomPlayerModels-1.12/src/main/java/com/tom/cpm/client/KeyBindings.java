package com.tom.cpm.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.tom.cpl.gui.IKeybind;
import com.tom.cpl.gui.KeyboardEvent;

public class KeyBindings implements IKeybind {
	private static KeyConflictCtx conflictCtx = new KeyConflictCtx();
	public static KeyBinding gestureMenuBinding, renderToggleBinding;
	public static Map<Integer, KeyBinding> quickAccess = new HashMap<>();

	public static void init() {
		gestureMenuBinding = new KeyBinding("key.cpm.gestureMenu", KeyConflictContext.IN_GAME, Keyboard.KEY_G, "key.cpm.category");
		kbs.add(new KeyBindings(gestureMenuBinding, "gestureMenu"));

		renderToggleBinding = new KeyBinding("key.cpm.renderToggle", KeyConflictContext.IN_GAME, 0, "key.cpm.category");
		kbs.add(new KeyBindings(renderToggleBinding, "renderToggle"));

		for(int i = 1;i<=6;i++)
			createQA(i);
	}

	private static class KeyConflictCtx implements IKeyConflictContext {

		@Override
		public boolean isActive() {
			Minecraft mc = Minecraft.getMinecraft();
			return mc.currentScreen instanceof GuiImpl || (!KeyConflictContext.GUI.isActive() && mc.player != null);
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return other == this || other == KeyConflictContext.IN_GAME;
		}
	}

	private static void createQA(int id) {
		KeyBinding kb = new KeyBinding("key.cpm.qa_" + id, conflictCtx, 0, "key.cpm.category");
		kbs.add(new KeyBindings(kb, "qa_" + id));
		quickAccess.put(id, kb);
	}

	public static List<IKeybind> kbs = new ArrayList<>();
	private final KeyBinding kb;
	private final String name;

	private KeyBindings(KeyBinding kb, String name) {
		this.kb = kb;
		this.name = name;
		ClientRegistry.registerKeyBinding(kb);
	}

	@Override
	public boolean isPressed(KeyboardEvent evt) {
		return kb.isActiveAndMatches(evt.keyCode);
	}

	@Override
	public String getBoundKey() {
		return kb.getDisplayName();
	}

	@Override
	public String getName() {
		return name;
	}
}

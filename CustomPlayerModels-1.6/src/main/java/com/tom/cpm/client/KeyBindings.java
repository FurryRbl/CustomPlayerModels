package com.tom.cpm.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import com.tom.cpl.gui.IKeybind;
import com.tom.cpl.gui.KeyboardEvent;

public class KeyBindings implements IKeybind {
	public static KeyBinding gestureMenuBinding, renderToggleBinding;
	public static IKeybind[] quickAccess = new IKeybind[IKeybind.QUICK_ACCESS_KEYBINDS_COUNT];

	public static void init() {
		gestureMenuBinding = new KeyBinding("key.cpm.gestureMenu", Keyboard.KEY_G);
		kbs.add(new KeyBindings(gestureMenuBinding, "gestureMenu"));

		renderToggleBinding = new KeyBinding("key.cpm.renderToggle", 0);
		kbs.add(new KeyBindings(renderToggleBinding, "renderToggle"));

		for(int i = 1;i<=IKeybind.QUICK_ACCESS_KEYBINDS_COUNT;i++)
			createQA(i);
	}

	private static void createQA(int id) {
		KeyBinding kb = new KeyBinding("key.cpm.qa_" + id, 0);
		KeyBindings kbs = new KeyBindings(kb, "qa_" + id);
		KeyBindings.kbs.add(kbs);
		quickAccess[id - 1] = kbs;
	}

	public static List<IKeybind> kbs = new ArrayList<>();
	private final KeyBinding kb;
	private final String name;

	private KeyBindings(KeyBinding kb, String name) {
		this.kb = kb;
		this.name = name;
		KeyBinding.keybindArray.add(kb);
	}

	@Override
	public boolean isPressed(KeyboardEvent evt) {
		return kb.keyCode == evt.keyCode;
	}

	@Override
	public String getBoundKey() {
		return GameSettings.getKeyDisplayString(kb.keyCode);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isPressed() {
		return kb.isPressed();
	}
}

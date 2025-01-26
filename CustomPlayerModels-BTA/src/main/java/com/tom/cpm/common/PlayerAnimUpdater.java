package com.tom.cpm.common;

import java.util.function.BiConsumer;

import net.minecraft.core.entity.player.Player;

import com.tom.cpm.shared.animation.ServerAnimationState;

public class PlayerAnimUpdater implements BiConsumer<Player, ServerAnimationState> {

	@Override
	public void accept(Player t, ServerAnimationState u) {
		u.updated = true;
		//u.creativeFlying = t.flySpeed;
		u.falling = t.fallDistance;
		u.health = t.getHealth() / t.getMaxHealth();
		u.air = Math.max(t.airSupply / 300f, 0);
		//u.hunger = t.getFoodStats().getFoodLevel() / 20f;
		u.inMenu = t.craftingInventory != t.inventorySlots;
	}

}

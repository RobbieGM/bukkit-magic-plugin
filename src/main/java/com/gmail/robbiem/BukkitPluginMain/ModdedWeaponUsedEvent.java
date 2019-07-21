package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModdedWeaponUsedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	Player attacker;
	
	public ModdedWeaponUsedEvent(Player attacker) {
		this.attacker = attacker;
	}
	
	public Player getAttacker() {
		return attacker;
	}

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

}

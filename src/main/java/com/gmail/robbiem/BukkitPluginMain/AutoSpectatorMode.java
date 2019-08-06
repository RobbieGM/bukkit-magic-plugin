package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AutoSpectatorMode implements Listener {
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.getEntity().setGameMode(GameMode.SPECTATOR);
	}
}

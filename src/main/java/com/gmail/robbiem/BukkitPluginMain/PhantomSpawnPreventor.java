package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class PhantomSpawnPreventor implements Listener {
	
	@EventHandler
	public void onPhantomSpawn(CreatureSpawnEvent e) {
		if (e.getEntityType() == EntityType.PHANTOM && e.getSpawnReason() == SpawnReason.NATURAL) {
			e.getEntity().remove();
		}
	}
	
}

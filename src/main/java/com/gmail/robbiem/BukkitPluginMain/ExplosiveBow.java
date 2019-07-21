package com.gmail.robbiem.BukkitPluginMain;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosiveBow implements Listener {
	JavaPlugin plugin;
	
	public ExplosiveBow(JavaPlugin p) {
		plugin = p;
	}
	
	ArrayList<UUID> explosiveArrowIDs = new ArrayList<UUID>();
	
	@EventHandler
	public void onArrowShoot(EntityShootBowEvent e) {
		if (e.getBow().getItemMeta().getDisplayName().equals("Explosive Bow")) {
			explosiveArrowIDs.add(e.getProjectile().getUniqueId());
			LivingEntity entity = e.getEntity();
			if (entity instanceof Player)
				plugin.getServer().getPluginManager().callEvent(new ModdedWeaponUsedEvent((Player) e.getEntity()));
		}
	}
	
	@EventHandler
	public void onArrowLand(ProjectileHitEvent e) {
		if (e.getEntityType() == EntityType.ARROW && explosiveArrowIDs.contains(e.getEntity().getUniqueId())) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				TNTPrimed tnt = (TNTPrimed) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(0);
				tnt.setYield(3f);
				explosiveArrowIDs.remove(e.getEntity().getUniqueId());
				e.getEntity().remove();
			}, (int) (20 * 0.5));
		}
	}
}

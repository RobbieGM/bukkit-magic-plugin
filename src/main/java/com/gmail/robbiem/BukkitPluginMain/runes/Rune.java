package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public abstract class Rune implements Listener {
	
	JavaPlugin plugin;
	
	public Rune(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	abstract String getRuneName();
	
	static Entity getDamageSource(Entity damager) {
		if (damager instanceof Projectile) {
			ProjectileSource source = ((Projectile) damager).getShooter();
			if (source instanceof Entity) {
				damager = (Entity) source;
			}
		}
		return damager;
	}
	
	static void playRuneEffect(Player p) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
		p.getWorld().spawnParticle(Particle.SPELL, p.getLocation(), 10, 1, 1, 1);
	}
	
	boolean playerHasRune(Player p) {
		ItemStack item = new ItemStack(Material.EMERALD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getRuneName());
		item.setItemMeta(meta);
		return p.getInventory().containsAtLeast(item, 1);
	}
	
	void consumeRune(Player p) {
		for (ItemStack stack: p.getInventory().getContents()) {
			if (stack != null && stack.getType() == Material.EMERALD && stack.getItemMeta() != null &&
				stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().equals(getRuneName())) {
				stack.setAmount(stack.getAmount() - 1);
			}
		}
	}
}

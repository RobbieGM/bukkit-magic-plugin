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
import org.bukkit.projectiles.ProjectileSource;

import com.gmail.robbiem.BukkitPluginMain.Main;

public abstract class Rune implements Listener {

	Main plugin;

	public Rune(Main plugin) {
		this.plugin = plugin;
	}

	public abstract String getName();

	public abstract String getLore();

	/**
	 * @return The number of runes that will be yielded when this is crafted from 8
	 *         runes and a center item. Should be <= 8.
	 */
	int getCraftingYield() {
		return 1;
	}

	public abstract Material getCraftingRecipeCenterItem();

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

	boolean playerHasRune(Player player) {
		return getRuneItem(player) != null;
	}

	void consumeRune(Player holder) {
		ItemStack stack = getRuneItem(holder);
		stack.setAmount(stack.getAmount() - 1);
	}

	private ItemStack getRuneItem(Player holder) {
		for (ItemStack stack : holder.getInventory().getContents()) {
			if (stack != null && stack.getType() == Material.EMERALD && stack.getItemMeta() != null
					&& stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().equals(getName())) {
				return stack;
			}
		}
		return null;
	}
}

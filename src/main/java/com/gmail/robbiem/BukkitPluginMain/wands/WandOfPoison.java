package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfPoison extends Wand implements ParticleWand {
	
	public WandOfPoison(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		cast(player, plugin, (location) -> {
			location.getWorld().spawnParticle(Particle.SNEEZE, location, 100, 0.5, 0.5, 0.5, 0.2);
		}, (entity, spellLocation) -> {
			entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, isBuffed ? 20 * 7 : 20 * 5, 255));
		});
		return true;
	}
	
	@Override
	public float getSpeed() {
		return isBuffed ? 1f : 0.75f;
	}

	@Override
	public long getItemCooldown() {
		return 500l;
	}
	
	@Override
	public long getPlayerCooldown() {
		return 300l;
	}

	@Override
	public String getLore() {
		return "Poisons players that it hits\nfor 5 seconds";
	}

	@Override
	public int getRange() {
		return 50;
	}

	@Override
	public float getEffectRadius() {
		return 3.5f;
	}

	@Override
	public void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.SNEEZE, particleLocation, 5, 0, 0, 0, 0.05);
	}

	@Override
	public Material getWandTip() {
		return Material.SPIDER_EYE;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Poison";
	}
	
}

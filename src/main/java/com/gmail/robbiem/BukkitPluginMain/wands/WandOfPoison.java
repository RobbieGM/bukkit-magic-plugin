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

public class WandOfPoison extends ParticleWand {
	
	public WandOfPoison(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		cast(player, plugin, (location) -> {
			location.getWorld().spawnParticle(Particle.SNEEZE, location, 100, 0.5, 0.5, 0.5, 0.2);
		}, (entity, spellLocation) -> {
			entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 255));
		});
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 500l;
	}

	@Override
	public String getLore() {
		return "Poisons players that it hits\nfor 5 seconds";
	}

	@Override
	int getRange() {
		return 40;
	}

	@Override
	float getEffectRadius() {
		return 3.5f;
	}

	@Override
	void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.SNEEZE, particleLocation, 5, 0, 0, 0, 0.05);
	}

	@Override
	public Material getWandTip() {
		return Material.SPIDER_EYE;
	}

	@Override
	public String getName() {
		return "Wand of Poison";
	}
	
}

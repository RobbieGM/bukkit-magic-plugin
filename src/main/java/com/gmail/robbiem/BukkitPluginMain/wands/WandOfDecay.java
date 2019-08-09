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

public class WandOfDecay extends Wand implements ParticleWand {
	
	public WandOfDecay(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		cast(player, plugin, (location) -> {
			location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 50, 0, 0, 0, 0.15);
		}, (entity, spellLocation) -> {
			entity.damage(1, player);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 4, 4));
		});
		return true;
	}
	
	@Override
	public float getSpeed() {
		return 0.5f;
	}

	@Override
	public long getPlayerCooldown() {
		return 750l;
	}

	@Override
	public String getLore() {
		return "This wand will apply the withering\nstatus effect to all it touches.";
	}

	@Override
	public int getRange() {
		return 40;
	}

	@Override
	public float getEffectRadius() {
		return 4f;
	}

	@Override
	public void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLocation, 0, 0, -1, 0, 0.05);
	}

	@Override
	public Material getWandTip() {
		return Material.WITHER_ROSE;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Decay";
	}

}

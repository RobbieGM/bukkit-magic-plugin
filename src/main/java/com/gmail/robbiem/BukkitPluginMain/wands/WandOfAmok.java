package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfAmok extends Wand implements ParticleWand {

	public WandOfAmok(Main plugin) {
		super(plugin);
	}

	@Override
	public int getRange() {
		return 40;
	}

	@Override
	public float getEffectRadius() {
		return 4;
	}

	@Override
	public void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, particleLocation, 1);
	}

	@Override
	public Material getWandTip() {
		return Material.BONE;
	}

	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		cast(player, plugin, (location) -> {
			LivingEntity target = null;
			for (LivingEntity entity : world.getLivingEntities()) {
				if (entity.getLocation().distance(location) < 5 && (target == null
						|| entity.getLocation().distance(location) < target.getLocation().distance(location))) {
					target = entity;
				}
			}
			if (target == null)
				return;
			final LivingEntity foundTarget = target;
			for (Mob mob : world.getEntitiesByClass(Mob.class).stream().filter(
					mob -> mob.getLocation().distance(location) < 30 && mob.getUniqueId() != foundTarget.getUniqueId())
					.collect(Collectors.toList())) {
				mob.setTarget(foundTarget);
				mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));
			}
		}, (entity, spellLocation) -> {
		});
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0l;
	}

	@Override
	public long getItemCooldown() {
		return 2000l;
	}

	@Override
	public String getName() {
		return "Wand of Amok";
	}

	@Override
	public String getLore() {
		return "When a beam cast from this wand\nhits its target, all nearby mobs\ntake a sudden interest in it.";
	}

}

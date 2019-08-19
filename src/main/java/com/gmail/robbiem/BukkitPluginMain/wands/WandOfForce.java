package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfForce extends LeftClickableWand {

	public WandOfForce(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		List<LivingEntity> mobs = world.getLivingEntities();
		boolean pushedMob = false;
		for (LivingEntity mob: mobs) {
			pushedMob = true;
			if (mob instanceof Player && ((Player) mob).getGameMode() != GameMode.SURVIVAL)
				continue;
			Vector mobLocation = mob.getLocation().toVector();
			Vector playerLocation = player.getLocation().toVector();
			if (mobLocation.isInSphere(playerLocation, 10) && !mobLocation.equals(playerLocation)) {
				double distance = mobLocation.clone().subtract(playerLocation).length();
				double force = isBuffed ? (10 - 1 * distance) : (7 - 0.7 * distance); // Same location: 10, 10 blocks away: 0
				Vector propelVector = mobLocation.subtract(playerLocation).normalize().multiply(force);
				propelVector.setY(0.8);
				world.spawnParticle(Particle.SPELL_INSTANT, mob.getLocation(), 5, 1, 1, 1);
				mob.setVelocity(propelVector);
			}
		}
		if (pushedMob)
			world.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.5f);
		return pushedMob;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		world.spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0, 0, 0, 0.05);
		player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 2500l; 
	}
	
	@Override
	public long getAltPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getAltItemCooldown() {
		return 1000l;
	}
	
	@Override
	public String getLore() {
		return "Pushes all mobs and players away from\nyou with considerable velocity";
	}

	@Override
	public Material getWandTip() {
		return Material.GUNPOWDER;
	}

	@Override
	public String getName() {
		return "Wand of Force";
	}

}

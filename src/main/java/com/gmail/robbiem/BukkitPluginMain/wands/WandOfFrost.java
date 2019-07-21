package com.gmail.robbiem.BukkitPluginMain.wands;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WandOfFrost extends Wand {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Block targeted = player.getTargetBlock(null, 40);
		Location eyeLocation = player.getEyeLocation();
		Vector difference = targeted.getLocation().toVector().subtract(eyeLocation.toVector());
		double distance = difference.length();
		for (int i = 1; i < distance; i++) {
			final int finalI = i;
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				Location particleLocation = eyeLocation.toVector().add(difference.clone().multiply(finalI / distance)).toLocation(world);
				world.spawnParticle(Particle.END_ROD, particleLocation, 5, 0, 0, 0, 0.02);
			}, i);
		}
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			world.spawnParticle(Particle.SNOWBALL, targeted.getLocation(), 20, 0.5, 0.5, 0.5);
			world.playSound(targeted.getLocation(), Sound.BLOCK_CHORUS_FLOWER_DEATH, 1, 1.5f);
			for (LivingEntity e: world.getLivingEntities()) {
				if (e.getLocation().distanceSquared(targeted.getLocation()) < 5 * 5) {
					e.damage(4, player);
					e.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 128)); // Jump boost 128 = no jumping to evade slowness
					e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 255));
				}
			}
		}, (long) distance);
	}

	@Override
	public long getCooldown() {
		return 1500l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  b", " s ", "p  ").setIngredient('b', Material.SNOWBALL).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

}

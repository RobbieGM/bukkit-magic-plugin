package com.gmail.robbiem.BukkitPluginMain.wands;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WandOfFrost extends ParticleWand implements Listener {
	
	Map<UUID, Long> frozenPlayers = new HashMap<>();

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		cast(player, plugin, (location) -> {
			world.spawnParticle(Particle.SNOWBALL, location, 20, 0.5, 0.5, 0.5);
			world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_DEATH, 1, 1.5f);
		}, (entity, location) -> {
			entity.damage(4, player);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 128)); // Jump boost 128 = no jumping to evade slowness
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 255));
			if (entity instanceof Player)
				frozenPlayers.put(entity.getUniqueId(), new Date().getTime() + 1000 * 3);
		});
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		UUID id = e.getPlayer().getUniqueId();
		if (frozenPlayers.containsKey(id) && frozenPlayers.get(id) > new Date().getTime()) {
			Location from = e.getFrom().clone();
			from.setPitch(e.getTo().getPitch());
			from.setYaw(e.getTo().getYaw());
			e.setTo(from);
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public long getCooldown() {
		return 1500l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		return startingRecipe.shape("  b", " s ", "p  ").setIngredient('b', Material.SNOWBALL).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}

	@Override
	public String getLore() {
		return "Casts a hex which freezes and slightly damages mobs\nand players unlucky enough to be within 5 blocks\nof its destination point";
	}

	@Override
	int getRange() {
		return 40;
	}

	@Override
	float getEffectRadius() {
		return 5;
	}

	@Override
	void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.END_ROD, particleLocation, 5, 0, 0, 0, 0.02);
	}

}

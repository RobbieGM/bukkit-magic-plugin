package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfEquineSummoning extends Scroll {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		Horse horse = (Horse) world.spawnEntity(player.getLocation(), EntityType.HORSE);
		horse.setTamed(true);
		horse.setOwner(player);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
		horse.addPassenger(player);
		world.playSound(player.getLocation(), Sound.ENTITY_HORSE_AMBIENT, 1, 1);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			horse.playEffect(EntityEffect.ENTITY_POOF);
			world.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
			horse.remove();
		}, 20 * 30);
	}

	@Override
	public long getCooldown() {
		return 100l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.LEATHER;
	}

	@Override
	public String getLore() {
		return "Summons a horse that you\ncan ride for 30 seconds";
	}

}

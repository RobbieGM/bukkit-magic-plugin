package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ScrollOfSurprise extends Scroll {

	@Override
	public void use(ItemStack item, Player player, World world, JavaPlugin plugin, Server server) {
		List<Player> opponents = world.getPlayers().stream().filter(p -> !player.equals(p)).filter(p -> p.getGameMode() == GameMode.SURVIVAL).collect(Collectors.toList());
		if (opponents.size() > 0) {
			Player opp = opponents.get((int) (Math.random() * opponents.size()));
			world.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
			world.playSound(opp.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				player.teleport(opp);
				world.playSound(opp.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
			}, 20);
		} else {
			player.sendMessage("There is nobody to teleport to.");
		}
	}

	@Override
	public long getCooldown() {
		return 500l;
	}

	@Override
	public String getLore() {
		return "Teleport to a random opponent.";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ENDER_PEARL;
	}

}

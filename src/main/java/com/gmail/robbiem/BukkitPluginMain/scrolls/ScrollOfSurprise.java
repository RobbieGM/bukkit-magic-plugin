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
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfSurprise extends Scroll {

	public ScrollOfSurprise(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		List<Player> opponents = world.getPlayers().stream().filter(p -> !player.equals(p)).filter(p -> p.getGameMode() == GameMode.SURVIVAL).collect(Collectors.toList());
		if (opponents.size() > 0) {
			Player opp = opponents.get((int) (Math.random() * opponents.size()));
			opp.closeInventory();
			world.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
			world.playSound(opp.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
			server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				player.teleport(opp);
				world.playSound(opp.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
			}, 20 * 6);
			return true;
		} else {
			player.sendMessage("There is nobody to teleport to.");
			return false;
		}
	}

	@Override
	public long getPlayerCooldown() {
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

	@Override
	public String getName() {
		return "Scroll of Surprise";
	}

}

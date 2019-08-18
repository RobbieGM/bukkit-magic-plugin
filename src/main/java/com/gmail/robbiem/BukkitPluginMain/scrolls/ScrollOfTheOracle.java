package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.robbiem.BukkitPluginMain.Main;
import java.util.function.Function;

public class ScrollOfTheOracle extends Scroll {

	public ScrollOfTheOracle(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		List<Player> opponents = world.getPlayers().stream().filter(p -> !player.equals(p)).filter(p -> p.getGameMode() == GameMode.SURVIVAL).collect(Collectors.toList());
		Player opp = null;
		if (opponents.size() > 0) {
			opp = opponents.get((int) (Math.random() * opponents.size()));
		}
		CrystalBall ball = new CrystalBall(player, opp, plugin);
		player.getInventory().addItem(ball.item);
		world.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.GHAST_TEAR;
	}

	@Override
	public String getLore() {
		return "Gives you a crystal ball revealing the\nend coordinates of the world border";
	}

	@Override
	public String getName() {
		return "Scroll of the Oracle";
	}

}

class CrystalBall {
	Player holder;
	Player opponent;
	ItemStack item;
	
	CrystalBall(Player holder, Player opponent, JavaPlugin plugin) {
		this.opponent = opponent;
		this.holder = holder;
		this.item = new ItemStack(Material.HEART_OF_THE_SEA, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Crystal Ball");
		item.setItemMeta(meta);
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::doTick, 0, 3 * 20);
	}
	
	void doTick() {
		List<String> data = new ArrayList<>();
		Location center = holder.getWorld().getWorldBorder().getCenter();
		data.add("Center X: " + center.getBlockX() + ", Z: " + center.getBlockZ());
		if (opponent != null && opponent.getGameMode() == GameMode.SURVIVAL) {
			data.add("Opp. dist: " + (int) opponent.getLocation().distance(holder.getLocation()));
		} else {
			data.add("Center dist: " + (int) center.distance(holder.getLocation()));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setLore(data);
		item.setItemMeta(meta);
		updateItem();
	}
	
	void updateItem() {
		Function<ItemStack, Boolean> isCrystalBall = (ItemStack stack) -> stack != null && stack.getType() == Material.HEART_OF_THE_SEA && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().equals("Crystal Ball");
		int crystalBallIndex = Arrays.asList(holder.getInventory().getContents()).stream().map(isCrystalBall).collect(Collectors.toList()).indexOf(true);
		if (crystalBallIndex != -1) {
			holder.getInventory().setItem(crystalBallIndex, item);
		}
//		} else {
//			holder.getInventory().addItem(item);
//		}
	}
}

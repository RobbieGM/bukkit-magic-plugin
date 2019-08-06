package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfJealousy extends Scroll implements Listener {

	public ScrollOfJealousy(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Player closest = null;
		for (Player p: world.getPlayers()) {
			if ((closest == null || p.getLocation().distanceSquared(player.getLocation()) < closest.getLocation().distanceSquared(player.getLocation())) && p.getGameMode() == GameMode.SURVIVAL && !p.equals(player)) {
				closest = p;
			}
		}
		if (closest != null) {
			player.openInventory(closest.getInventory());
			return true;
		} else {
			player.sendMessage("No other players were detected. Are they in survival mode?");
			return false;
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory();
		if (inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if (holder instanceof Player && e.getWhoClicked() instanceof Player && !holder.equals(e.getWhoClicked())) {
			e.setCancelled(true);
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}

	@Override
	public String getLore() {
		return "Just how screwed are you?\nFind out with this scroll that lets you\nsee the nearest player's inventory.";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.BARREL;
	}

	@Override
	public String getName() {
		return "Scroll of Jealousy";
	}

}

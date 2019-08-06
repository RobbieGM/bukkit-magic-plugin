package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfPlunder extends Scroll implements Listener {
	
	public ScrollOfPlunder(Main plugin) {
		super(plugin);
	}

	static final int NUM_CHESTS = 4;

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.CHEST;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		boolean isFull = player.getInventory().firstEmpty() == -1;
		if (isFull) {
			player.sendMessage("Your inventory is full!");
			return false;
		} else {
			ItemStack chest = new ItemStack(Material.CHEST, NUM_CHESTS);
			ItemMeta meta = chest.getItemMeta();
			meta.setDisplayName("Loot Chest");
			chest.setItemMeta(meta);
			player.getInventory().addItem(chest);
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (e.getBlock().getType() == Material.CHEST &&
			player != null &&
			e.getItemInHand().getItemMeta().hasDisplayName() &&
			e.getItemInHand().getItemMeta().getDisplayName().equals("Loot Chest")) {
			Chest chest = (Chest) e.getBlock().getState();
			chest.setLootTable(player.getServer().getLootTable(new NamespacedKey("hungergames", "chests/loot_table")));
			player.openInventory(chest.getInventory());
			player.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
				System.out.println("Chest contains sugarcane: " + chest.getInventory().contains(Material.SUGAR_CANE));
				chest.getInventory().remove(Material.SUGAR_CANE);
			}, 2);
//			chest.getInventory().remove(Material.PRISMARINE_SHARD);
			chest.update();
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
		return "Not feeling happy with your loot?\nHere's " + NUM_CHESTS + " more chests\nfor you!";
	}

	@Override
	public String getName() {
		return "Scroll of Plunder";
	}

}

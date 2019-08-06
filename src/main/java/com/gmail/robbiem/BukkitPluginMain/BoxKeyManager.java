package com.gmail.robbiem.BukkitPluginMain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.block.Action;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class BoxKeyManager implements Listener {
	
	Map<String, Location> keyNameToBoxLocation = new HashMap<>();
	Map<World, Map<String, Inventory>> worldToBoxList = new HashMap<>();
	Main plugin;
	
	public BoxKeyManager(Main plugin) {
		this.plugin = plugin;
		World world = plugin.getServer().getWorld(Main.DEFAULT_WORLD_NAME);
		keyNameToBoxLocation.put("\u2020 Combat box key", new Location(world, 930, 70, 1501));
		keyNameToBoxLocation.put("\u00A4 Seer box key", new Location(world, 932, 70, 1501));
		keyNameToBoxLocation.put("\u2588 Librarian box key", new Location(world, 934, 70, 1501));
		keyNameToBoxLocation.put("\u256C Ninja box key", new Location(world, 936, 70, 1501));
		keyNameToBoxLocation.put("\u263C Naturalist box key", new Location(world, 936, 70, 1509));
		keyNameToBoxLocation.put("\u2261 Archer box key", new Location(world, 934, 70, 1509));
		keyNameToBoxLocation.put("\u03A9 Frostmage box key", new Location(world, 932, 70, 1509));
		keyNameToBoxLocation.put("\u25B2 Pyromancer box key", new Location(world, 930, 70, 1509));
		resetWorldBoxes(plugin.primaryWorld);
		// This class should be a Listener that assigns each player an inventory for each box (duplicate keys do not result in more items)
	} // Player, World, Key name --> Inventory
	
	public void resetWorldBoxes(World world) {
		Map<String, Inventory> boxMap = new HashMap<>();
		keyNameToBoxLocation.forEach((String keyName, Location boxLoc) -> {
			ShulkerBox state = (ShulkerBox) boxLoc.getBlock().getState();
			boxMap.put(keyName, copyInventory(state.getInventory()));
		});
		worldToBoxList.put(world, boxMap);
	}
	
	/**
	 * Copies the inventory, used for removing the lock on inventories.
	 * @param orig The original inventory
	 * @return A copy of the inventory
	 */
	Inventory copyInventory(Inventory orig) {
		Inventory inv = plugin.getServer().createInventory(null, orig.getType());
		inv.setContents(orig.getContents());
		return inv;
	}
	
	@EventHandler
	public void onKeyUse(PlayerInteractEvent e) {
		if (e.getItem() != null &&
			e.getItem().getItemMeta() != null &&
			e.getItem().getItemMeta().hasDisplayName() &&
			keyNameToBoxLocation.containsKey(e.getItem().getItemMeta().getDisplayName()) &&
			Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(e.getAction())) {
			
			World world = e.getPlayer().getWorld();
			if (!worldToBoxList.containsKey(world)) {
				resetWorldBoxes(world);
			}
			Inventory box = worldToBoxList.get(world).get(e.getItem().getItemMeta().getDisplayName());
			e.getPlayer().openInventory(box);
		}
	}
	
//	ItemStack item(Material material) {
//		return new ItemStack(material, 1);
//	}
//	
//	ItemStack item(Material material, int count) {
//		return new ItemStack(material, count);
//	}
//	
//	ItemStack item(Material material, int count, String displayName) {
//		ItemStack stack = item(material, count);
//		ItemMeta meta = stack.getItemMeta();
//		meta.setDisplayName(displayName);
//		stack.setItemMeta(meta);
//		return stack;
//	}
//	
//	Inventory getCombatBox(Inventory i) {
//		ItemStack combatBow = item(Material.BOW);
//		combatBow.addEnchantment(new EnchantmentWrapper("punch"), 1);
//		combatBow.addEnchantment(new EnchantmentWrapper("infinity"), 1);
//		combatBow.addEnchantment(new EnchantmentWrapper("power"), 3);
//		i.addItem(item(Material.BOW));
//		ItemStack trident = item(Material.TRIDENT);
//		trident.addEnchantment(new EnchantmentWrapper("loyalty"), 3);
//		i.addItem(trident);
//		i.addItem(item(Material.DIAMOND_HELMET));
//		i.addItem(item(Material.DIAMOND_CHESTPLATE));
//		i.addItem(item(Material.DIAMOND_LEGGINGS));
//		i.addItem(item(Material.DIAMOND_BOOTS));
//		ItemStack sword = item(Material.DIAMOND_SWORD);
//		sword.addEnchantment(new EnchantmentWrapper("knockback"), 2);
//		i.addItem(sword);
//		i.addItem(item(Material.ARROW));
//		i.addItem(item(Material.SPECTRAL_ARROW, 10));
//		ItemStack harmingArrows = item(Material.TIPPED_ARROW, 5);
//		PotionMeta harmMeta = (PotionMeta) harmingArrows.getItemMeta();
//		harmMeta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
//		harmingArrows.setItemMeta(harmMeta);
//		i.addItem(harmingArrows);
//		i.addItem(item(Material.SHIELD));
//		i.addItem(item(Material.COOKED_BEEF, 10));
//		return i;
//	}
//	
//	Inventory getSeerBox(Inventory i) {
//		i.addItem(item(Material.PAPER, 2, "Scroll of the Hunter's Vision"));
//		i.addItem(item(Material.PAPER, 1, "Scroll of the Oracle"));
//		i.addItem(item(Material.PAPER, 2, "Scroll of the Invisibility"));
//		i.addItem(item(Material.PAPER, 3, "Scroll of the Eagle"));
//		for (int j = 0; j < 3; j++) {
//			ItemStack nightVision = item(Material.POTION);
//			PotionMeta nvMeta = (PotionMeta) nightVision.getItemMeta();
//			nvMeta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
//			nightVision.setItemMeta(nvMeta);
//			i.addItem(nightVision);
//		}
//		return i;
//	}
//	
//	Inventory getLibrarianBox(Inventory i) {
//		i.addItem(item(Material.ANVIL));
//		i.addItem(item(Material.LAPIS_LAZULI, 64));
//		i.addItem(item(Material.ENCHANTING_TABLE));
//		i.addItem(item(Material.BOOKSHELF, 15));
//		List<EnchantmentWrapper> enchantments = Arrays.asList(
//				new EnchantmentWrapper("blast_protection")
//				);
//		return i;
//	}
}

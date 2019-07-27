/**
 * 
 */
package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Robbie
 *
 */
public class Main extends JavaPlugin implements Listener {
	
	public static final String HUNGER_GAMES_WORLD = "HungerGames";
	public static final String HUNGER_GAMES_WAITING_ROOM = "Hunger Games Waiting Room";
	
	ModdedItemManager moddedItemManager;
	World hungerGames;
	
	public Main() {
		moddedItemManager = new ModdedItemManager(this);
	}
	
	@Override
	public void onEnable() {
		getLogger().info("Plugin was enabled.");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new BlockProtector(), this);
		pm.registerEvents(moddedItemManager, this);
//		pm.registerEvents(new EventSnooper(this), this);
		pm.registerEvents(new AutoSpectatorMode(), this);
		pm.registerEvents(new PhantomSpawnPreventor(), this);
		moddedItemManager.onEnable();
		this.getCommand("go").setExecutor(new GoCommand(this));
		this.getCommand("go-again").setExecutor(new RestartGameCommand(this));
		ShapedRecipe tridentRecipe = new ShapedRecipe(new NamespacedKey(this, "custom_trident"), new ItemStack(Material.TRIDENT, 1));
		tridentRecipe.shape("dsd", " s ", " s ").setIngredient('d', Material.DIAMOND).setIngredient('s', Material.STICK);
		getServer().addRecipe(tridentRecipe);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin was disabled.");
		getServer().resetRecipes();
		moddedItemManager.onDisable();
	}
	
//	@Override
//	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
//		return new CustomChunkGenerator();
//	}
	
    /**
     * Stops the player from being able to rename items with an anvil in survival, because modded item identification relies on item names.
     * @param event
     */
	@EventHandler
    public void onPlayerRenameItem(InventoryClickEvent event) {
    	if (event.getView().getType() == InventoryType.ANVIL &&
    		event.getView().getPlayer().getGameMode() != GameMode.CREATIVE &&
    		event.getRawSlot() == 2 &&
    		event.getView().getItem(0).getType() != Material.AIR &&
    		event.getView().getItem(2).getType() != Material.AIR &&
    		event.getView().getItem(0).getItemMeta().getDisplayName() != event.getView().getItem(2).getItemMeta().getDisplayName()) {
    		event.setCancelled(true);
    	}
    }
	
	@EventHandler
	public void onPlayerCraftMagicItem(CraftItemEvent e) {
		if (e.getView().getType() == InventoryType.WORKBENCH &&
			e.getInventory().getResult().getItemMeta() != null &&
			(e.getInventory().getResult().getItemMeta().getDisplayName().startsWith("Wand of") ||
			e.getInventory().getResult().getItemMeta().getDisplayName().startsWith("Scroll of"))) {
			e.getWhoClicked().getWorld().playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1);
		}
	}
}

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Robbie
 *
 */
public class Main extends JavaPlugin implements Listener {
	
	public static final String DEFAULT_WORLD_NAME = "HungerGames";
	public String primaryWorldName = "HungerGames";
	public World primaryWorld;
	
	public ModdedItemManager moddedItemManager;
	BoxKeyManager boxKeyManager;
	World hungerGames;
	
	public Main() {
		moddedItemManager = new ModdedItemManager(this);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(primaryWorld.getSpawnLocation()); // The player by default respawns in the default world rather than the current one
	}
	
	@Override
	public void onEnable() {
		primaryWorld = getServer().getWorlds().get(0);
		getLogger().info("Plugin was enabled.");
		boxKeyManager = new BoxKeyManager(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new BlockProtector(), this);
		pm.registerEvents(moddedItemManager, this);
		pm.registerEvents(boxKeyManager, this);
//		pm.registerEvents(new EventSnooper(this), this);
		pm.registerEvents(new AutoSpectatorMode(), this);
		pm.registerEvents(new PhantomSpawnPreventor(), this);
		moddedItemManager.onEnable();
		this.getCommand("go").setExecutor(new GoCommand(this));
		this.getCommand("new-world").setExecutor(new CreateNewWorldCommand(this));
		ShapedRecipe tridentRecipe = new ShapedRecipe(new NamespacedKey(this, "custom_trident"), new ItemStack(Material.TRIDENT, 1));
		tridentRecipe.shape("dsd", " s ", " s ").setIngredient('d', Material.DIAMOND).setIngredient('s', Material.STICK);
		ShapelessRecipe enderPearlFromGhastTear = new ShapelessRecipe(new NamespacedKey(this, "ender_pearl_from_tear"), new ItemStack(Material.ENDER_PEARL, 1));
		enderPearlFromGhastTear.addIngredient(2, Material.GHAST_TEAR);
		ShapelessRecipe ghastTearsFromEnderPearl = new ShapelessRecipe(new NamespacedKey(this, "tears_from_ender_pearl"), new ItemStack(Material.GHAST_TEAR, 2));
		ghastTearsFromEnderPearl.addIngredient(1, Material.ENDER_PEARL);
		getServer().addRecipe(tridentRecipe);
		getServer().addRecipe(enderPearlFromGhastTear);
		getServer().addRecipe(ghastTearsFromEnderPearl);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin was disabled.");
		getServer().resetRecipes();
		moddedItemManager.onDisable();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().teleport(primaryWorld.getSpawnLocation());
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

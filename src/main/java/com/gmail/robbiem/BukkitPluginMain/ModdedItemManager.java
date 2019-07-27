package com.gmail.robbiem.BukkitPluginMain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.robbiem.BukkitPluginMain.runes.*;
import com.gmail.robbiem.BukkitPluginMain.scrolls.*;
import com.gmail.robbiem.BukkitPluginMain.wands.*;

public class ModdedItemManager implements Listener {
	
	Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();
	Map<String, Wand> wands = new HashMap<>();
	Map<String, Scroll> scrolls = new HashMap<>();
	List<Rune> runes = new ArrayList<>();
	JavaPlugin plugin;
	static final List<Material> SHULKER_BOXES = Arrays.asList(new Material[] {Material.RED_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX});
	static final List<Material> UNBREAKABLE = Arrays.asList(new Material[] {Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK});
	List<Material> unbreakableAndShulkers = new ArrayList<Material>();
	Glow glow;
	
	public ModdedItemManager(JavaPlugin plugin) {
		this.plugin = plugin;
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			glow = new Glow(new NamespacedKey(plugin, "glow"));
			Enchantment.registerEnchantment(glow);
		} catch (Exception e) {
			plugin.getLogger().info("Registering enchantment threw error because the enchantment was already registered (this is probably fine)");
		}
		unbreakableAndShulkers.addAll(UNBREAKABLE);
		unbreakableAndShulkers.addAll(SHULKER_BOXES);
	}
	
	void initItems() {
		wands.put("Wand of Teleportation", new WandOfTeleportation());
		wands.put("Wand of Flame", new WandOfFlame());
		wands.put("Wand of Force", new WandOfForce());
		wands.put("Wand of Avalanche", new WandOfAvalanche(unbreakableAndShulkers));
		wands.put("Wand of Arrowstorm", new WandOfArrowStorm());
		wands.put("Wand of Polymorph", new WandOfPolymorph());
		wands.put("Wand of Lava Bolt", new WandOfLavaBolt(unbreakableAndShulkers));
		wands.put("Wand of Frost", new WandOfFrost());
		wands.put("Wand of Lightning", new WandOfLightning());
		wands.put("Wand of Magic Missile", new WandOfMagicMissile());
		wands.put("Wand of Poison", new WandOfPoison());
		wands.put("Wand of Decay", new WandOfDecay());
		wands.put("Wand of Magnetism", new WandOfMagnetism());
		wands.put("Wand of OP", new WandOfOP());
		scrolls.put("Scroll of Flight", new ScrollOfFlight());
		scrolls.put("Scroll of Teleportation", new ScrollOfTeleportation());
		scrolls.put("Scroll of Equine Summoning", new ScrollOfEquineSummoning());
		scrolls.put("Scroll of the Oracle", new ScrollOfTheOracle());
		scrolls.put("Scroll of the Hunter's Vision", new ScrollOfTheHuntersVision());
		scrolls.put("Scroll of Invisibility", new ScrollOfInvisibility(plugin));
		scrolls.put("Scroll of the Eagle", new ScrollOfTheEagle());
		scrolls.put("Scroll of Upgrade", new ScrollOfUpgrade());
		scrolls.put("Scroll of Scavenging", new ScrollOfScavenging());
		scrolls.put("Scroll of Blindness", new ScrollOfBlindness());
		scrolls.put("Scroll of Protection", new ScrollOfProtection(unbreakableAndShulkers));
		scrolls.put("Scroll of Necromancy", new ScrollOfNecromancy());
		scrolls.put("Scroll of Jealousy", new ScrollOfJealousy());
		scrolls.put("Scroll of Surprise", new ScrollOfSurprise());
		runes.add(new RuneOfVengeance(plugin));
		runes.add(new RuneOfBackstabbing(plugin));
		runes.add(new RuneOfInvincibility(plugin));
		runes.add(new RuneOfInfestation(plugin));
		runes.add(new RuneOfBounce(plugin));
		runes.add(new RuneOfDisarmament(plugin));
	}
	
	public void onEnable() {
		initItems();
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new GrassOfHerbalHealing(), plugin);
		pm.registerEvents(new WellNourishedSapling(), plugin);
		pm.registerEvents(new ChunkPlacer(unbreakableAndShulkers), plugin);
		pm.registerEvents(new ChunkMiner(unbreakableAndShulkers), plugin);
		pm.registerEvents(new ExplosiveBow(plugin), plugin);
		ExplosiveBow.registerCraftingRecipe(plugin);
		EnchantedSnowball snowballListener = new EnchantedSnowball();
		snowballListener.registerRecipe(plugin);
		pm.registerEvents(snowballListener, plugin);
		
		wands.forEach((String wandName, Wand wand) -> {
			ShapedRecipe startingWandRecipe = createRecipeFromResult(Material.STICK, wandName, wand.getLore());
			ShapedRecipe wandRecipe = wand.getCraftingRecipeFromResultingItem(startingWandRecipe);
			if (wandRecipe != null) {
				plugin.getServer().addRecipe(wandRecipe);
			}
			if (wand.isEventHandler() && wand instanceof Listener) {
				pm.registerEvents((Listener) wand, plugin);
			}
		});
		scrolls.forEach((String scrollName, Scroll scroll) -> {
			ShapedRecipe scrollRecipe = createRecipeFromResult(Material.PAPER, scrollName, scroll.getLore());
			scrollRecipe.shape("ppp", "pxp", "ppp").setIngredient('p', Material.PAPER).setIngredient('x', scroll.getCraftingRecipeCenterItem());
			if (scrollRecipe != null) {
				plugin.getServer().addRecipe(scrollRecipe);
			}
			if (scroll.isEventHandler() && scroll instanceof Listener) {
				pm.registerEvents((Listener) scroll, plugin);
			}
		});
		runes.forEach((Rune rune) -> {
			pm.registerEvents(rune, plugin);
		});
	}
	
	public void onDisable() {
		wands.clear();
		scrolls.clear();
		runes.clear();
	}
	
	ShapedRecipe createRecipeFromResult(Material itemMaterial, String name, String lore) {
		ItemStack item = new ItemStack(itemMaterial);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore.split("\n")));
		meta.addEnchant(glow, 1, true);
		item.setItemMeta(meta);
		String keyName = name.toLowerCase().replaceAll("[^a-z0-9/._-]", "");
		return new ShapedRecipe(new NamespacedKey(plugin, keyName), item);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
		World world = player.getWorld();
		boolean isRightClick = e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK;
		boolean isOtherInteraction = e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().isInteractable();
		
		if (item != null && playerMayUseSpell(player) && isRightClick && !isOtherInteraction) {
			String itemName = item.getItemMeta().getDisplayName();
			boolean isStick = item.getType() == Material.STICK;
			boolean isPaper = item.getType() == Material.PAPER;
			
			if (wands.containsKey(itemName) && isStick) {
				Wand wand = wands.get(itemName);
				wand.use(item, player, world, plugin, plugin.getServer());
				setPlayerCooldown(player, wand.getCooldown());
				if (wand.isWeapon())
					plugin.getServer().getPluginManager().callEvent(new ModdedWeaponUsedEvent(player));
			}
			
			if (scrolls.containsKey(itemName) && isPaper) {
				Scroll scroll = scrolls.get(itemName);
				scroll.use(item, player, world, plugin, plugin.getServer());
				item.setAmount(item.getAmount() - 1);
				setPlayerCooldown(player, scroll.getCooldown());
			}
		}
	}
	
	public boolean playerMayUseSpell(Player player) {
		if (!cooldowns.containsKey(player.getUniqueId()))
			return true;
		long now = new Date().getTime();
		return now > cooldowns.get(player.getUniqueId());
	}
	
	public void setPlayerCooldown(Player player, long cooldownMillis) {
		cooldowns.put(player.getUniqueId(), new Date().getTime() + cooldownMillis);
	}
}

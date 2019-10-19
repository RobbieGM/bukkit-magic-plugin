package com.gmail.robbiem.BukkitPluginMain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gmail.robbiem.BukkitPluginMain.runes.Rune;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfBackstabbing;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfBounce;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfDisarmament;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfFeatherFalling;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfInfestation;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfInvincibility;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfPsionicBlast;
import com.gmail.robbiem.BukkitPluginMain.runes.RuneOfVengeance;
import com.gmail.robbiem.BukkitPluginMain.scrolls.Scroll;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfAntiMagic;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfBlindness;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfElements;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfEquineSummoning;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfFlight;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfInvisibility;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfJealousy;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfNecromancy;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfOrganization;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfPlunder;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfProtection;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfScavenging;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfSurprise;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfTeleportation;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfTheEagle;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfTheHuntersVision;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfTheOracle;
import com.gmail.robbiem.BukkitPluginMain.scrolls.ScrollOfUpgrade;
import com.gmail.robbiem.BukkitPluginMain.wands.EnderWand;
import com.gmail.robbiem.BukkitPluginMain.wands.LeftClickableWand;
import com.gmail.robbiem.BukkitPluginMain.wands.VampiricWand;
import com.gmail.robbiem.BukkitPluginMain.wands.Wand;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfArchitecture;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfArrowStorm;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfAvalanche;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfBlasting;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfConfusion;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfDecay;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfDestruction;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfFlak;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfFlame;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfForce;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfForceField;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfFrost;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfGrappling;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfLavaBolt;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfLevitation;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfLightning;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfMagicMissile;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfMagnetism;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfOP;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfPerception;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfPoison;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfPolymorph;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfPoseidon;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfPropulsion;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfScience;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfTeleportation;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfTransparency;
import com.gmail.robbiem.BukkitPluginMain.wands.WandOfTrapping;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

public class ModdedItemManager implements Listener {

	public PlayerCooldownManager cooldownManager;
	List<Wand> wands = new ArrayList<>();
	List<Scroll> scrolls = new ArrayList<>();
	List<Rune> runes = new ArrayList<>();
	Main plugin;
	static final List<Material> SHULKER_BOXES = Arrays.asList(Material.RED_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
			Material.WHITE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX,
			Material.YELLOW_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX);
	static final List<Material> UNBREAKABLE = Arrays.asList(Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK,
			Material.CHAIN_COMMAND_BLOCK);
	public static final Material LESSER_WAND_BASE = Material.GHAST_TEAR;
	public static final List<Material> UNBREAKABLE_AND_SHULKERS = Stream
			.concat(SHULKER_BOXES.stream(), UNBREAKABLE.stream()).collect(Collectors.toList());
	Glow glow;

	public ModdedItemManager(Main plugin) {
		cooldownManager = new PlayerCooldownManager(plugin);
		this.plugin = plugin;
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			glow = new Glow(new NamespacedKey(plugin, "glow"));
			Enchantment.registerEnchantment(glow);
		} catch (Exception e) {
			plugin.getLogger().info(
					"Registering enchantment threw error because the enchantment was already registered (this is probably fine)");
		}

	}

	<T> List<T> createInstancesOfClasses(List<Class<? extends T>> classes) {
		return classes.stream().map(clazz -> {
			try {
				return clazz.getConstructor(Main.class).newInstance(plugin);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	void initItems() {
		List<Class<? extends Wand>> wandClasses = Arrays.asList(EnderWand.class, VampiricWand.class,
				WandOfArchitecture.class, WandOfArrowStorm.class, WandOfAvalanche.class, WandOfBlasting.class,
				WandOfConfusion.class, WandOfDecay.class, WandOfDestruction.class, WandOfFlak.class, WandOfFlame.class,
				WandOfForce.class, WandOfForceField.class, WandOfFrost.class, WandOfGrappling.class, WandOfLavaBolt.class,
				WandOfLevitation.class, WandOfLightning.class, WandOfMagicMissile.class, WandOfMagnetism.class, WandOfOP.class,
				WandOfPerception.class, WandOfPoison.class, WandOfPolymorph.class, WandOfPoseidon.class, WandOfPropulsion.class,
				WandOfScience.class, WandOfTeleportation.class, WandOfTransparency.class, WandOfTrapping.class);
		List<Class<? extends Scroll>> scrollClasses = Arrays.asList(ScrollOfAntiMagic.class, ScrollOfBlindness.class,
				ScrollOfElements.class, ScrollOfEquineSummoning.class, ScrollOfFlight.class, ScrollOfInvisibility.class,
				ScrollOfJealousy.class, ScrollOfNecromancy.class, ScrollOfOrganization.class, ScrollOfPlunder.class,
				ScrollOfProtection.class, ScrollOfScavenging.class, ScrollOfSurprise.class, ScrollOfTeleportation.class,
				ScrollOfTheEagle.class, ScrollOfTheHuntersVision.class, ScrollOfTheOracle.class, ScrollOfUpgrade.class);
		List<Class<? extends Rune>> runeClasses = Arrays.asList(RuneOfBackstabbing.class, RuneOfBounce.class,
				RuneOfDisarmament.class, RuneOfFeatherFalling.class, RuneOfInfestation.class, RuneOfInvincibility.class,
				RuneOfPsionicBlast.class, RuneOfVengeance.class);
		wands = createInstancesOfClasses(wandClasses);
		for (int i = 0; i < 2; i++) {
			Wand buffed = wands.get((int) (Math.random() * wands.size()));
			plugin.getLogger().info("The " + buffed.getName() + " has been randomly selected to be buffed");
			buffed.isBuffed = true;
		}
		scrolls = createInstancesOfClasses(scrollClasses);
		runes = createInstancesOfClasses(runeClasses);
	}

	public void onEnable() {
		initItems();
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new GrassOfHerbalHealing(), plugin);
		pm.registerEvents(new WellNourishedSapling(), plugin);
		pm.registerEvents(new ChunkPlacer(UNBREAKABLE_AND_SHULKERS), plugin);
		pm.registerEvents(new ChunkMiner(UNBREAKABLE_AND_SHULKERS), plugin);
		pm.registerEvents(new ExplosiveBow(plugin), plugin);
		ExplosiveBow.registerCraftingRecipe(plugin);
		EnchantedSnowball snowballListener = new EnchantedSnowball();
		snowballListener.registerRecipe(plugin);
		pm.registerEvents(snowballListener, plugin);

		wands.forEach((Wand wand) -> {
			ShapedRecipe wandRecipe = createRecipeFromResult(Material.STICK, wand.getName(), wand.getLore());
			wandRecipe.shape("  t", " s ", "b  ").setIngredient('t', wand.getWandTip()).setIngredient('s', Material.STICK)
					.setIngredient('b', wand.getWandBase());
			plugin.getServer().addRecipe(wandRecipe);
			if (wand.isEventHandler() && wand instanceof Listener) {
				pm.registerEvents((Listener) wand, plugin);
			}
		});
		scrolls.forEach((Scroll scroll) -> {
			ShapedRecipe scrollRecipe = createRecipeFromResult(Material.PAPER, scroll.getName(), scroll.getLore());
			scrollRecipe.shape("ppp", "pxp", "ppp").setIngredient('p', Material.PAPER).setIngredient('x',
					scroll.getCraftingRecipeCenterItem());
			plugin.getServer().addRecipe(scrollRecipe);
			if (scroll.isEventHandler() && scroll instanceof Listener) {
				pm.registerEvents((Listener) scroll, plugin);
			}
		});
		runes.forEach((Rune rune) -> {
			pm.registerEvents(rune, plugin);
			ShapedRecipe runeRecipe = createRecipeFromResult(Material.EMERALD, rune.getName(), rune.getLore());
			runeRecipe.shape("eee", "exe", "eee").setIngredient('e', Material.EMERALD).setIngredient('x',
					rune.getCraftingRecipeCenterItem());
			plugin.getServer().addRecipe(runeRecipe);
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

	public <T extends UseableItem> T getUseableItem(List<T> items, String name) {
		Optional<T> optional = items.stream().filter(item -> item.getName().equals(name)).findFirst();
		return optional.isPresent() ? optional.get() : null;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		Player player = e.getPlayer();
		World world = player.getWorld();
		boolean isRightClick = e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK;
		boolean isLeftClick = e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK;
		boolean isOtherInteraction = e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getClickedBlock().getType().isInteractable()
				&& !e.getClickedBlock().getType().toString().contains("STAIRS");
		plugin.getLogger().info("Is other interaction? " + isOtherInteraction);
		boolean isMainHand = e.getHand() == EquipmentSlot.HAND;
		if (item != null && !isOtherInteraction && isMainHand) {
			String itemName = item.getItemMeta().getDisplayName();
			boolean isStick = item.getType() == Material.STICK;
			boolean isPaper = item.getType() == Material.PAPER;

			Wand wand = getUseableItem(wands, itemName);
			if (wand != null && isStick && (isRightClick || isLeftClick)) {
				if (cooldownManager.playerMayUseItem(player, wand)) {
					if (isLeftClick && wand instanceof LeftClickableWand)
						e.setCancelled(true);
					useWand(wand, item, player, isLeftClick);
				}
			}

			Scroll scroll = getUseableItem(scrolls, itemName);
			if (scroll != null && isPaper && isRightClick) {
				if (cooldownManager.playerMayUseItem(player, scroll)) {
					boolean scrollUsed = scroll.use(item, player, world, plugin.getServer());
					if (scrollUsed) {
						cooldownManager.useItem(player, scroll);
						item.setAmount(item.getAmount() - 1);
					}
				}
			}
		}
	}

	void useWand(Wand wand, ItemStack item, Player player, boolean useAlt) {
		boolean wandUsed = false;
		if (useAlt && wand instanceof LeftClickableWand)
			wandUsed = ((LeftClickableWand) wand).useAlt(item, player, player.getWorld(), plugin.getServer());
		else if (!useAlt)
			wandUsed = wand.use(item, player, player.getWorld(), plugin.getServer());
		if (wandUsed) {
			if (useAlt)
				cooldownManager.useItemAlt(player, (LeftClickableWand) wand);
			else
				cooldownManager.useItem(player, wand);
			if (wand.isWeapon()) {
				plugin.getServer().getPluginManager().callEvent(new ModdedWeaponUsedEvent(player));
			}
		}
	}
}

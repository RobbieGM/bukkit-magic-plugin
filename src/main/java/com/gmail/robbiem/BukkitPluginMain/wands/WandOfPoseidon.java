package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfPoseidon extends LeftClickableWand /*implements Listener*/ {

	public WandOfPoseidon(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Block b = player.getLocation().getBlock();
		if (!ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(b.getType()))
			b.setType(Material.WATER);
		return true;
	}

	@Override
	public long getAltPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getAltItemCooldown() {
		return 500l;
	}

	@Override
	public Material getWandTip() {
		return Material.WATER_BUCKET;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 20 * 10, 255));
		int dist = (int) Wand.getBlockTarget(player, 20, true).distance(player.getEyeLocation());
		if (dist == 0) return false;
		Predicate<Block> breakable = block -> !ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(block.getType());
		player.getLineOfSight(new HashSet<Material>(Arrays.asList(Material.WATER, Material.AIR)), dist).stream().filter(breakable).forEach(block -> {
			block.setType(Material.WATER);
//			block.setMetadata("disableFlow", new FixedMetadataValue(plugin, true));
		});
		return true;
	}
	
//	@EventHandler
//	public void onBlockSpread(BlockFromToEvent e) {
//		if (e.getBlock().hasMetadata("disableFlow")) {
//			e.setCancelled(true);
//		}
//	}
//	
//	@Override
//	public boolean isEventHandler() {
//		return true;
//	}

	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}
	
	@Override
	public long getItemCooldown() {
		return 2000l;
	}

	@Override
	public String getName() {
		return "Wand of Poseidon";
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getLore() {
		return "This wand shoots a stream of water\nthat can quickly turn barren\ndeserts into oceans!";
	}

}

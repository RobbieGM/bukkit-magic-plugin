package com.gmail.robbiem.BukkitPluginMain.wands;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfFrost extends LeftClickableWand implements ParticleWand, Listener {
	
	public WandOfFrost(Main plugin) {
		super(plugin);
	}

	Map<UUID, Long> frozenPlayers = new HashMap<>();

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		cast(player, plugin, (location) -> {
			world.spawnParticle(Particle.SNOWBALL, location, 20, 0.5, 0.5, 0.5);
			world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_DEATH, 1, 1.5f);
		}, (entity, location) -> {
			entity.damage(4, player);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 128)); // Jump boost 128 = no jumping to evade slowness
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 255));
			if (entity instanceof Player)
				frozenPlayers.put(entity.getUniqueId(), new Date().getTime() + 1000 * 3);
		});
		return true;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		final int radius = 5;
		Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.ALWAYS);
		if (targetBlock != null) {
			Location target = targetBlock.getLocation();
			world.playSound(target, Sound.BLOCK_CHORUS_FLOWER_DEATH, 1, 1.5f);
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						Location l = target.clone().add(x, y, z);
						boolean isBreakable = !ModdedItemManager.UNBREAKABLE_AND_SHULKERS.contains(l.getBlock().getType());
						boolean isSolidOrWater = !l.getBlock().isPassable() || l.getBlock().getType() == Material.WATER;
						if (l.distanceSquared(target) <= radius * radius && isBreakable && isSolidOrWater) {
							l.getBlock().setType(Material.ICE);
						}
					}
				}
			}
			return true;
		} else return false;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		UUID id = e.getPlayer().getUniqueId();
		if (frozenPlayers.containsKey(id) && frozenPlayers.get(id) > new Date().getTime()) {
			Location from = e.getFrom().clone();
			from.setPitch(e.getTo().getPitch());
			from.setYaw(e.getTo().getYaw());
			e.setTo(from);
		}
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 500l;
	}
	
	@Override
	public long getItemCooldown() {
		return isBuffed ? 2000l : 6000l;
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
	public String getLore() {
		return "Casts a hex which freezes and slightly damages mobs\nand players unlucky enough to be within 5 blocks\nof its destination point";
	}

	@Override
	public int getRange() {
		return 40;
	}

	@Override
	public float getEffectRadius() {
		return isBuffed ? 6f : 4f;
	}

	@Override
	public void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.END_ROD, particleLocation, 5, 0, 0, 0, 0.02);
	}

	@Override
	public Material getWandTip() {
		return Material.SNOWBALL;
	}

	@Override
	public String getName() {
		return "Wand of Frost";
	}

}

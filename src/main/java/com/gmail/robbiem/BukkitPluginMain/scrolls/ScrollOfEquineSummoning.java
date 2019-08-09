package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfEquineSummoning extends Scroll {

	public ScrollOfEquineSummoning(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		Horse horse = (Horse) world.spawnEntity(player.getLocation(), EntityType.HORSE);
		horse.setTamed(true);
		horse.setOwner(player);
		horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
		horse.setJumpStrength(2);
		horse.setAdult();
		horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 2));
		horse.setInvulnerable(true);
		horse.addPassenger(player);
		world.playSound(player.getLocation(), Sound.ENTITY_HORSE_AMBIENT, 1, 1);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 100l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.LEATHER;
	}

	@Override
	public String getLore() {
		return "Summons a horse that you\ncan ride for 30 seconds";
	}

	@Override
	public String getName() {
		return "Scroll of Equine Summoning";
	}

}

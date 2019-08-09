package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfElements extends Scroll {

	public ScrollOfElements(Main plugin) {
		super(plugin);
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.DIRT;
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		Blaze blaze = (Blaze) world.spawnEntity(player.getLocation().add(0, 3, 0), EntityType.BLAZE);
		server.getScoreboardManager().getMainScoreboard().getTeam(player.getName()).addEntry(blaze.getUniqueId().toString());	
		((CraftEntity) blaze).getHandle().setInvisible(true);
		blaze.setInvulnerable(true);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}

	@Override
	public String getLore() {
		return "Summons an elemental to fight for you.";
	}

	@Override
	public String getName() {
		return "Scroll of Elements";
	}

}
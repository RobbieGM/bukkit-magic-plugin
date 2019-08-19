package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfPolymorph extends LeftClickableWand implements Listener {

	public WandOfPolymorph(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
//		EntityType[] types = {EntityType.SHEEP, EntityType.COW, EntityType.WOLF, EntityType.OCELOT, EntityType.HORSE, EntityType.LLAMA, EntityType.IRON_GOLEM};
		boolean wasUsed = false;
		for (LivingEntity e: world.getLivingEntities()) {
			double dist = e.getLocation().distance(player.getLocation());
			if (dist > (isBuffed ? 25 : 15)) continue;
			if (e instanceof Player) continue;
			if (e instanceof Wolf) continue;
			wasUsed = true;
			e.playEffect(EntityEffect.ENTITY_POOF);
			e.remove();
//			EntityType type = types[(int) (Math.random() * types.length)];
			Wolf wolf = (Wolf) world.spawnEntity(e.getLocation(), EntityType.WOLF);
			wolf.setOwner(player);
		}
		return wasUsed;
	}
	
	@Override
	public boolean useAlt(ItemStack item, Player player, World world, Server server) {
		Location clickLoc = Wand.getTarget(player, 20, false);
		if (clickLoc == null) return false;
		boolean wasUsed = false;
		for (LivingEntity e: world.getLivingEntities()) {
			double dist = e.getLocation().distance(clickLoc);
			if (dist > 3) continue;
			if (e instanceof Player) continue;
			if (e instanceof Wolf) continue;
			wasUsed = true;
			e.playEffect(EntityEffect.ENTITY_POOF);
			Team team = server.getScoreboardManager().getMainScoreboard().getTeam(player.getName());
			if (team != null) {
				team.addEntry(e.getUniqueId().toString());
			}
		}
		return wasUsed;
	}

	@Override
	public long getPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getItemCooldown() {
		return 1500l;
	}
	
	@Override
	public long getAltPlayerCooldown() {
		return 0;
	}
	
	@Override
	public long getAltItemCooldown() {
		return 2500l;
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "Turns the nearest mob within 5 blocks\ninto a random friendly creature";
	}

	@Override
	public Material getWandTip() {
		return Material.WHITE_WOOL;
	}
	
	@Override
	public Material getWandBase() {
		return ModdedItemManager.LESSER_WAND_BASE;
	}

	@Override
	public String getName() {
		return "Wand of Polymorph";
	}

}

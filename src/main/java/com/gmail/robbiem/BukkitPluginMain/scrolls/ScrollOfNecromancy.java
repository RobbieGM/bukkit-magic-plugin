package com.gmail.robbiem.BukkitPluginMain.scrolls;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;

public class ScrollOfNecromancy extends Scroll {
	
	public ScrollOfNecromancy(Main plugin) {
		super(plugin);
	}

	static final EntityType[] MONSTERS = new EntityType[] {EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.DROWNED, EntityType.CREEPER};
	static final int QUANTITY = 7;
	static final float RADIUS = 5;

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		for (Player p: world.getPlayers()) {
//			if (p.equals(player)) continue;
			Location middle = p.getLocation();
			for (int i = 0; i < QUANTITY; i++) {
				double theta = 2 * Math.PI * i / QUANTITY;
				Location mobLocation = middle.clone().add(new Vector(Math.cos(theta), 0, Math.sin(theta)).multiply(RADIUS));
				mobLocation.setY(world.getHighestBlockYAt(mobLocation));
				EntityType monsterType = MONSTERS[(int) (Math.random() * MONSTERS.length)];
				Mob e = (Mob) world.spawnEntity(mobLocation, monsterType);
				e.playEffect(EntityEffect.ENTITY_POOF);
				server.getScoreboardManager().getMainScoreboard().getTeam(player.getName()).addEntry(e.getUniqueId().toString());
				if (!p.equals(player)) // Make monsters not attack the scroll user
					e.setTarget(p);
			}
		}
		return true;
	}

	@Override
	public long getItemCooldown() {
		return 5000l;
	}

	@Override
	public long getPlayerCooldown() {
		return 1000l;
	}
	
	@Override
	public String getLore() {
		return "This scroll will summon " + QUANTITY + " undead\nmonsters around all your opponents.";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.ROTTEN_FLESH;
	}

	@Override
	public String getName() {
		return "Scroll of Necromancy";
	}

}

package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfPolymorph extends Wand implements Listener {

	public WandOfPolymorph(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		EntityType[] types = {EntityType.SHEEP, EntityType.COW, EntityType.WOLF, EntityType.OCELOT, EntityType.HORSE, EntityType.LLAMA, EntityType.IRON_GOLEM};
		boolean wasUsed = false;
		for (LivingEntity e: world.getLivingEntities()) {
			double dist = e.getLocation().distance(player.getLocation());
			if (dist > 15) continue;
			if (e instanceof Player) continue;
			wasUsed = true;
			e.playEffect(EntityEffect.ENTITY_POOF);
			e.remove();
			EntityType type = types[(int) (Math.random() * types.length)];
			world.spawnEntity(e.getLocation(), type);
		}
		return wasUsed;
	}

	@Override
	public long getPlayerCooldown() {
		// TODO Auto-generated method stub
		return 1500l;
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
	public String getName() {
		return "Wand of Polymorph";
	}

}

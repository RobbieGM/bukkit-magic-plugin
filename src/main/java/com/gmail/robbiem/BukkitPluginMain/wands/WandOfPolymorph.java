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
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class WandOfPolymorph extends Wand implements Listener {

	@Override
	public void use(ItemStack wandItem, Player player, World world, JavaPlugin plugin, Server server) {
		EntityType[] types = {EntityType.SHEEP, EntityType.COW, EntityType.WOLF, EntityType.OCELOT, EntityType.HORSE, EntityType.LLAMA, EntityType.IRON_GOLEM};
		for (LivingEntity e: world.getLivingEntities()) {
			double dist = e.getLocation().distance(player.getLocation());
			if (dist > 15) continue;
			if (e instanceof Player) continue;
			e.playEffect(EntityEffect.ENTITY_POOF);
			e.remove();
			EntityType type = types[(int) (Math.random() * types.length)];
			world.spawnEntity(e.getLocation(), type);
		}
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return 1500l;
	}

	@Override
	public ShapedRecipe getCraftingRecipeFromResultingItem(ShapedRecipe startingRecipe) {
		// TODO Auto-generated method stub
		return startingRecipe.shape("  w", " s ", "p  ").setIngredient('w', Material.WHITE_WOOL).setIngredient('s', Material.STICK).setIngredient('p', Material.ENDER_PEARL);
	}
	
	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public String getLore() {
		return "Turns the nearest mob within 5 blocks\ninto a random friendly creature";
	}

}

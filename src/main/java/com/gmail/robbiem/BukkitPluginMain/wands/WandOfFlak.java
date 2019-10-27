package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class WandOfFlak extends Wand implements ParticleWand {

	public WandOfFlak(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean use(ItemStack item, Player player, World world, Server server) {
		this.cast(player, plugin, (location) -> {
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			world.createExplosion(x, y, z, 1f, false, false);
		}, (entity, location) -> {
			if (entity.isGliding()) {
				entity.setVelocity(entity.getVelocity().multiply(isBuffed ? 0.1 : 0.5));
				entity.damage(4); // , player);
			}
			if (entity instanceof Player) {
				Player hitPlayer = (Player) entity;
				ItemStack elytra = hitPlayer.getInventory().getChestplate();
				if (elytra != null && elytra.getType() == Material.ELYTRA && entity.isGliding()) {
					Damageable meta = (Damageable) elytra.getItemMeta();
					meta.setDamage(meta.getDamage() + (isBuffed ? 200 : 150));
					elytra.setItemMeta((ItemMeta) meta);
					hitPlayer.getInventory().setChestplate(elytra);
				}
			}
		}, true);
		return true;
	}

	@Override
	public long getPlayerCooldown() {
		return 200l;
	}

	@Override
	public float getSpeed() {
		return 5f;
	}

	@Override
	public String getLore() {
		return "This wand does heavy damage to flying entities\nand damages players' elytras.";
	}

	@Override
	public int getRange() {
		int min = 20;
		int max = 60;
		return min + (int) (Math.random() * (max - min));
	}

	@Override
	public float getEffectRadius() {
		return isBuffed ? 10f : 8f;
	}

	@Override
	public void spawnWandParticle(Location particleLocation) {
		particleLocation.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, particleLocation, 0, 0, 0, 0, 0);
	}

	@Override
	public Material getWandTip() {
		return Material.FIREWORK_STAR;
	}

	@Override
	public String getName() {
		return "Wand of Flak";
	}

}

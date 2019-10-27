package com.gmail.robbiem.BukkitPluginMain.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

public class WandOfHarming extends Wand implements ParticleWand {

  public WandOfHarming(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack wandItem, Player player, World world, Server server) {
    cast(player, plugin, (location) -> {
      location.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, 50, 0, 0, 0, 0.15);
    }, (entity, spellLocation) -> {
      entity.damage(5); // , player);
      entity.setVelocity(new Vector(0, 0, 0));
    });
    return true;
  }

  @Override
  public long getPlayerCooldown() {
    return 680l;
  }

  @Override
  public String getLore() {
    return "This apprentice's wand deals basic\ndamage to affected targets.";
  }

  @Override
  public int getRange() {
    return 60;
  }

  @Override
  public float getEffectRadius() {
    return 4f;
  }

  @Override
  public void spawnWandParticle(Location location) {
    location.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, 5, 0, 0, 0, 0.1);
  }

  @Override
  public Material getWandTip() {
    return Material.IRON_SWORD;
  }

  @Override
  public Material getWandBase() {
    return ModdedItemManager.LESSER_WAND_BASE;
  }

  @Override
  public String getName() {
    return "Wand of Harming";
  }

}

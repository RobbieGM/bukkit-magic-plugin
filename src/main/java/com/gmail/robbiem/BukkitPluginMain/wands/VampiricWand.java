package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VampiricWand extends Wand implements ParticleWand {

  public VampiricWand(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack wandItem, Player player, World world, Server server) {
    cast(player, plugin, (location) -> {
      location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 50, 0, 0, 0, 0.15);
    }, (entity, spellLocation) -> {
      double damage = isBuffed ? 5 : 3;
      if (entity.getHealth() >= damage + 2) { // leave at least 1 heart
        entity.damage(1); // , player);
        entity.setHealth(entity.getHealth() - 2);
        player.setHealth(Math.min(player.getHealth() + 2, 20));
      }
    });
    return true;
  }

  @Override
  public long getPlayerCooldown() {
    return 750l;
  }

  @Override
  public long getItemCooldown() {
    return 2000l;
  }

  @Override
  public String getLore() {
    return "This wand will suck the very soul out of your foes.\nOr any living being you can find.\nHowever, it cannot kill.";
  }

  @Override
  public int getRange() {
    return 60;
  }

  @Override
  public float getEffectRadius() {
    return 5f;
  }

  @Override
  public void spawnWandParticle(Location particleLocation) {
    particleLocation.getWorld().spawnParticle(Particle.HEART, particleLocation, 0, 0, -1, 0, 0.05);
  }

  @Override
  public Material getWandTip() {
    return Material.POPPY;
  }

  @Override
  public String getName() {
    return "Wand of Bloodsucking";
  }

}

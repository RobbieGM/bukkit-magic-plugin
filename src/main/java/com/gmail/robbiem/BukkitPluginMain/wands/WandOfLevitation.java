package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.inventory.ItemStack;

public class WandOfLevitation extends Wand {

  public WandOfLevitation(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack wandItem, Player player, World world, Server server) {
    Location hit = Wand.getTarget(player, 80, false);
    if (hit != null) {
      LivingEntity closestToHit = null;
      for (LivingEntity entity : world.getLivingEntities()) {
        double dist = entity.getLocation().distanceSquared(hit);
        if (entity.getUniqueId() != player.getUniqueId() && dist < 10 * 10
            && (closestToHit == null || dist < closestToHit.getLocation().distanceSquared(hit))) {
          closestToHit = entity;
        }
      }
      if (closestToHit != null) {
        ShulkerBullet bullet = (ShulkerBullet) world
            .spawnEntity(player.getEyeLocation().add(player.getLocation().getDirection()), EntityType.SHULKER_BULLET);
        bullet.setShooter(player);
        bullet.setTarget(closestToHit);
        return true;
      }
    }
    return false;
  }

  @Override
  public long getPlayerCooldown() {
    return 500l;
  }

  @Override
  public long getItemCooldown() {
    return isBuffed ? 550l : 750l;
  }

  @Override
  public String getLore() {
    return "Spawns a shulker bullet to smite whoever\nor whatever you're looking at.";
  }

  @Override
  public Material getWandTip() {
    return Material.FEATHER;
  }

  @Override
  public String getName() {
    return "Wand of Levitation";
  }

}

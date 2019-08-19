package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

public class WandOfBlasting extends Wand {

  public WandOfBlasting(Main plugin) {
    super(plugin);
  }

  @Override
  public Material getWandTip() {
    return Material.TNT;
  }

  @Override
  public boolean use(ItemStack item, Player player, World world, Server server) {
    for (int delay = 0; delay < getEffectDuration() * 20 / 1000; delay += 3) {
      server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
        tnt.setYield(2f);
        tnt.setVelocity(player.getLocation().getDirection().multiply(3));
        tnt.setFuseTicks(20);
      }, delay);
    }
    return true;
  }

  long getEffectDuration() {
    return 2000l;
  }

  @Override
  public long getPlayerCooldown() {
    return getEffectDuration();
  }

  @Override
  public long getItemCooldown() {
    return 10000l;
  }

  @Override
  public String getName() {
    return "Wand of Blasting";
  }

  @Override
  public String getLore() {
    return "This wand shoots a large quantity\nof TNT from its tip, but takes\na while to recharge.";
  }

}
package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
    for (int delay = 0; delay < getEffectDuration() * 20 / 1000; delay += 4) {
      server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(player.getEyeLocation().add(0, 1, 0), EntityType.PRIMED_TNT);
        Vector randomVec = new Vector(-1 + 2 * Math.random(), -1 + 2 * Math.random(), -1 + 2 * Math.random());
        randomVec.normalize();
        randomVec.multiply(0.2);
        tnt.setYield(4.5f);
        tnt.setVelocity(player.getLocation().getDirection().multiply(3).add(randomVec));
        tnt.setFuseTicks(40);
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
package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnderWand extends LeftClickableWand {

  public EnderWand(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack wandItem, Player player, World world, Server server) {
    Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(6))
        .toLocation(world, player.getLocation().getYaw(), player.getLocation().getPitch());
    DragonFireball fireball = world.spawn(loc, DragonFireball.class);
    // fireball.setYield(isBuffed ? 2.5f : 2.0f);
    fireball.setShooter(player);
    world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1f, 4f);
    return true;
  }

  @Override
  public boolean useAlt(ItemStack item, Player player, World world, Server server) {
    Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection()).toLocation(world,
        player.getLocation().getYaw(), player.getLocation().getPitch());
    EnderPearl pearl = world.spawn(loc, EnderPearl.class);
    pearl.setVelocity(player.getLocation().getDirection());
    pearl.setShooter(player);
    return true;
  }

  @Override
  public long getItemCooldown() {
    return isBuffed ? 2000l : 2500l;
  }

  @Override
  public long getPlayerCooldown() {
    return 500l;
  }

  @Override
  public long getAltPlayerCooldown() {
    return 2000l;
  }

  @Override
  public long getAltItemCooldown() {
    return 2000l;
  }

  @Override
  public String getLore() {
    return "Launches a dragon fireball, or an ender pearl if you left-click.";
  }

  @Override
  public Material getWandTip() {
    return Material.STICK;
  }

  @Override
  public String getName() {
    return "Ender Wand";
  }

}

package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WandOfConfusion extends Wand implements ParticleWand {

  public WandOfConfusion(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack item, Player player, World world, Server server) {
    cast(player, plugin, collideLocation -> {
      world.spawnParticle(Particle.REDSTONE, collideLocation.add(0, 0.5, 0), 5, 0, 0, 0, 0.05,
          new Particle.DustOptions(Color.YELLOW, 3));
    }, (hitEntity, collideLocation) -> {
      if (hitEntity instanceof Player) {
        Player hit = (Player) hitEntity;
        hit.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 6, 255));
        shuffleInventory(hit);
        hit.getInventory().setHeldItemSlot((int) (Math.random() * 9));
        hit.setSprinting(false);
        Location newLoc = hit.getLocation();
        newLoc.setYaw((float) (Math.random() * 360));
        newLoc.setPitch((float) (-90 + 180 * Math.random()));
        hit.teleport(newLoc);
        hit.setVelocity(hit.getVelocity()
            .add(new Vector(-1 + 2 * Math.random(), Math.random(), -1 + 2 * Math.random()).normalize().multiply(0.2)));
      }
    });
    return true;
  }

  static void shuffleInventory(Player player) {
    List<ItemStack> inventory = Arrays.asList(player.getInventory().getStorageContents());
    List<ItemStack> hotbar = inventory.subList(0, 9);
    List<ItemStack> otherInventory = inventory.subList(9, inventory.size());
    Collections.shuffle(hotbar);
    Collections.shuffle(otherInventory);
    for (int j = 0; j < 9; j++) {
      inventory.set(j, hotbar.get(j));
    }
    for (int j = 9; j < inventory.size(); j++) {
      inventory.set(j, otherInventory.get(j - 9));
    }
    player.getInventory().setStorageContents(inventory.toArray(new ItemStack[inventory.size()]));
  }

  @Override
  public int getRange() {
    return 40;
  }

  @Override
  public float getEffectRadius() {
    return 2.5f;
  }

  @Override
  public void spawnWandParticle(Location particleLocation) {
    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, 0,
        new Particle.DustOptions(Color.YELLOW, 1.5f));
  }

  @Override
  public float getSpread() {
    return isBuffed ? 0 : 0.1f;
  }

  @Override
  public float getSpeed() {
    return isBuffed ? 2f : 1f;
  }

  @Override
  public Material getWandTip() {
    return Material.OBSIDIAN;
  }

  @Override
  public long getPlayerCooldown() {
    return 0;
  }

  @Override
  public long getItemCooldown() {
    return 1000l;
  }

  @Override
  public String getName() {
    return "Wand of Confusion";
  }

  @Override
  public String getLore() {
    return "This wand does all sorts of crazy\nand annoying things to players\nit hits.";
  }

}
package com.gmail.robbiem.BukkitPluginMain.wands;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WandOfTransparency extends Wand {

  public WandOfTransparency(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack item, Player player, World world, Server server) {
    for (double dist = 0; dist < 40; dist += 2) {
      Location location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(dist));
      for (Location blockLoc : getNearbyBlocks(location)) {
        player.sendBlockChange(blockLoc,
            server.createBlockData(blockLoc.getBlock().getType() == Material.AIR ? Material.AIR : Material.BARRIER));
      }
      server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
        for (Location blockLoc : getNearbyBlocks(location)) {
          player.sendBlockChange(blockLoc, blockLoc.getBlock().getBlockData());
        }
      }, getItemCooldown() * 20 / 1000);
    }
    return true;
  }

  Location[] getNearbyBlocks(Location center) {
    Location[] nearby = new Location[125];
    int i = 0;
    for (int x = -2; x <= 2; x++) {
      for (int y = -2; y <= 2; y++) {
        for (int z = -2; z <= 2; z++) {
          nearby[i] = center.clone().add(x, y, z);
          i++;
        }
      }
    }
    return nearby;
  }

  @Override
  public long getItemCooldown() {
    return 3000l;
  }

  @Override
  public long getPlayerCooldown() {
    return 0l;
  }

  @Override
  public String getLore() {
    return "Reveals obscured secrets by temporarily making blocks invisible.";
  }

  @Override
  public Material getWandTip() {
    return Material.GLASS;
  }

  @Override
  public Material getWandBase() {
    return ModdedItemManager.LESSER_WAND_BASE;
  }

  @Override
  public String getName() {
    return "Wand of Transparency";
  }

}

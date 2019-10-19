package com.gmail.robbiem.BukkitPluginMain.wands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedItemManager;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class WandOfPerception extends LeftClickableWand implements Listener {

  static final long CAMERA_VIEW_DURATION = 3000;
  Map<UUID, Camera> cameras = new HashMap<>();

  public WandOfPerception(Main plugin) {
    super(plugin);
  }

  @Override
  public boolean use(ItemStack wandItem, Player player, World world, Server server) {
    Camera camera = cameras.get(player.getUniqueId());
    if (camera != null) {
      GameMode originalGameMode = player.getGameMode();
      Location originalLocation = player.getLocation().clone();
      player.setGameMode(GameMode.SPECTATOR);
      player.teleport(camera.location.clone().add(0, -player.getEyeHeight(true), 0));
      camera.active = true;
      server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
        player.teleport(originalLocation);
        player.setGameMode(originalGameMode);
        camera.active = false;
      }, 20 * CAMERA_VIEW_DURATION / 1000);
      return true;
    } else {
      player.sendMessage("You need to place a camera (left-click on a block) before you can view through it.");
      return false;
    }
  }

  @Override
  public boolean useAlt(ItemStack item, Player player, World world, Server server) {
    Location cameraLocation = Wand.getBlockTarget(player, 5, false);
    if (cameraLocation != null) {
      world.spawnParticle(Particle.REDSTONE, cameraLocation, 3, new Particle.DustOptions(Color.WHITE, 3));
      cameras.put(player.getUniqueId(), new Camera(player, cameraLocation));
      return true;
    }
    return false;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player player = e.getPlayer();
    if (player.getGameMode().equals(GameMode.SPECTATOR) && cameras.containsKey(player.getUniqueId())
        && cameras.get(player.getUniqueId()).active) {
      Location from = e.getFrom().clone();
      from.setPitch(e.getTo().getPitch());
      from.setYaw(e.getTo().getYaw());
      e.setTo(from);
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    Player player = e.getPlayer();
    if (player.getGameMode() == GameMode.SPECTATOR && cameras.containsKey(player.getUniqueId())
        && cameras.get(player.getUniqueId()).active && e.getCause() == TeleportCause.SPECTATE) {
      e.setCancelled(true);
    }
  }

  @Override
  public boolean isEventHandler() {
    return true;
  }

  @Override
  public long getPlayerCooldown() {
    return CAMERA_VIEW_DURATION + 500l;
  }

  @Override
  public long getItemCooldown() {
    return CAMERA_VIEW_DURATION + 1000l;
  }

  @Override
  public long getAltPlayerCooldown() {
    return 0l;
  }

  @Override
  public long getAltItemCooldown() {
    return 1000l;
  }

  @Override
  public String getLore() {
    return "Left to click to place a camera,\nright click to view through it.";
  }

  @Override
  public Material getWandTip() {
    return Material.REDSTONE;
  }

  @Override
  public Material getWandBase() {
    return ModdedItemManager.LESSER_WAND_BASE;
  }

  @Override
  public String getName() {
    return "Wand of Perception";
  }

  @Override
  public boolean isWeapon() {
    return false;
  }

}

class Camera {
  Player placer;
  Location location;
  boolean active;

  public Camera(Player placer, Location location) {
    this.placer = placer;
    this.location = location;
    active = false;
  }
}

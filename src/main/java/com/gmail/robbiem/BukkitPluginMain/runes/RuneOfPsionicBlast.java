package com.gmail.robbiem.BukkitPluginMain.runes;

import java.util.List;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class RuneOfPsionicBlast extends Rune {

  public RuneOfPsionicBlast(Main plugin) {
    super(plugin);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerDamagedByEntity(EntityDamageByEntityEvent e) {
    if (e.getDamager() instanceof TNTPrimed && e.getDamager().hasMetadata("doesNotDamagePlayer") && e.getDamager().getMetadata("doesNotDamagePlayer").stream().anyMatch(metadataValue -> metadataValue.value().equals(e.getEntity().getUniqueId()))) {
      e.setCancelled(true);
      return;
    }
    if (!e.isCancelled() && e.getEntity() instanceof Player && playerHasRune((Player) e.getEntity())
        && ((Player) e.getEntity()).getHealth() - e.getFinalDamage() <= 6) {
      Player player = (Player) e.getEntity();
      consumeRune(player);
      playRuneEffect(player);
      e.setCancelled(true);
      TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
      tnt.setYield(6f);
      tnt.setFuseTicks(0);
      tnt.setMetadata("doesNotDamagePlayer", new FixedMetadataValue(plugin, player.getUniqueId()));
      List<LivingEntity> mobs = player.getWorld().getLivingEntities();
      for (LivingEntity mob : mobs) {
        if (mob instanceof Player && ((Player) mob).getGameMode() != GameMode.SURVIVAL)
          continue;
        Vector mobLocation = mob.getLocation().toVector();
        Vector playerLocation = player.getLocation().toVector();
        if (mobLocation.isInSphere(playerLocation, 10) && !mobLocation.equals(playerLocation)) {
          double distance = mobLocation.clone().subtract(playerLocation).length();
          double force = 7 - 0.7 * distance;
          Vector propelVector = mobLocation.subtract(playerLocation).normalize().multiply(force);
          propelVector.setY(0.8);
          player.getWorld().spawnParticle(Particle.SPELL_INSTANT, mob.getLocation(), 5, 1, 1, 1);
          mob.setVelocity(propelVector);
        }
      }
    }
  }

  @Override
  String getRuneName() {
    return "Rune of Psionic Blast";
  }

}
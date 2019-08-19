package com.gmail.robbiem.BukkitPluginMain.runes;

import com.gmail.robbiem.BukkitPluginMain.Main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RuneOfEscape extends Rune {

  public RuneOfEscape(Main plugin) {
    super(plugin);
  }

  @EventHandler
  public void onPlayerHurtByPlayer(EntityDamageByEntityEvent e) {
    Entity damager = getDamageSource(e.getDamager());
    if (!e.isCancelled() && e.getEntity() instanceof Player && damager instanceof Player
        && playerHasRune((Player) e.getEntity())) {
      Player damaged = (Player) e.getEntity();
      consumeRune(damaged);
      playRuneEffect(damaged);
      damaged.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 3));
    }
  }

  @Override
  public String getName() {
    return "Rune of Escape";
  }

  @Override
  public String getLore() {
    return "When attacked by a player,\nbecome faster.";
  }

  @Override
  public Material getCraftingRecipeCenterItem() {
    return Material.IRON_BOOTS;
  }

  @Override
  public int getCraftingYield() {
    return 2;
  }

}
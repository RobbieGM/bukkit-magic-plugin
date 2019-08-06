package com.gmail.robbiem.BukkitPluginMain.scrolls;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.gmail.robbiem.BukkitPluginMain.Main;
import com.gmail.robbiem.BukkitPluginMain.ModdedWeaponUsedEvent;

public class ScrollOfInvisibility extends Scroll implements Listener {
	
	public ScrollOfInvisibility(Main plugin) {
		super(plugin);
	}

	ArrayList<Player> invisiblePlayers = new ArrayList<>();
	JavaPlugin plugin;
	static final int LENGTH_SECONDS = 60;

	@Override
	public boolean use(ItemStack wandItem, Player player, World world, Server server) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * LENGTH_SECONDS, 1, false, false));
		setPlayerVisibility(player, false);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			setPlayerVisibility(player, true);
		}, 20 * LENGTH_SECONDS);
		return true;
	}
	
	public void setPlayerVisibility(Player player, boolean visible) {
		if (visible && invisiblePlayers.contains(player)) {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			for (Player p: player.getWorld().getPlayers()) {
				if (!p.equals(player))
					p.showPlayer(plugin, player);
			}
		}
		if (!visible) {
			invisiblePlayers.add(player);
			for (Player p: player.getWorld().getPlayers()) {
				if (!p.equals(player))
					p.hidePlayer(plugin, player);
			}
		}
	}
	
	@EventHandler
	public void onModdedWeaponUse(ModdedWeaponUsedEvent e) {
		setPlayerVisibility(e.getAttacker(), true);
	}
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
		Player damager = null;
		if (e.getEntity() instanceof Player) {
			damager = (Player) e.getEntity();
		}
		if (e.getEntity() instanceof Projectile) {
			ProjectileSource shooter = ((Projectile) e.getEntity()).getShooter();
			if (shooter instanceof Player)
				damager = (Player) shooter;
		}
		if (damager != null) {
			setPlayerVisibility(damager, true);
		}
	}

	@Override
	public long getPlayerCooldown() {
		// TODO Auto-generated method stub
		return 1000l;
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		// TODO Auto-generated method stub
		return Material.GOLDEN_CARROT;
	}
	
	@Override
	public boolean isEventHandler() {
		return true;
	}

	@Override
	public String getLore() {
		return "Makes you truly invisible for\na minute, or until you attack\nsomeone or use a wand";
	}

	@Override
	public String getName() {
		return "Scroll of Invisibility";
	}

}

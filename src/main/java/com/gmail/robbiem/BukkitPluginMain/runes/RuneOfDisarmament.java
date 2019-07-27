package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RuneOfDisarmament extends Rune {

	public RuneOfDisarmament(JavaPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	void onPlayerHitEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			Player attacked = (Player) e.getEntity();
			if (playerHasRune(attacker) && !attacker.equals(attacked)) {
				consumeRune(attacker);
				playRuneEffect(attacker);
				ItemStack heldItem = attacked.getInventory().getItemInMainHand();
				if (heldItem.getType() == Material.AIR)
					return;
				attacked.getInventory().setItemInMainHand(null);
				Item dropped = attacked.getWorld().dropItemNaturally(attacked.getLocation(), heldItem);
				dropped.setPickupDelay(40);
			}
		}
	}

	@Override
	String getRuneName() {
		return "Rune of Disarmament";
	}

}

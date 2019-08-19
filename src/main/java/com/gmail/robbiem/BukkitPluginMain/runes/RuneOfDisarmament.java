package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfDisarmament extends Rune {

	public RuneOfDisarmament(Main plugin) {
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
	public String getName() {
		return "Rune of Disarmament";
	}

	@Override
	public Material getCraftingRecipeCenterItem() {
		return Material.CHEST;
	}

	@Override
	public String getLore() {
		return "When attacking a player,\nmake them drop the item\nin their hand.";
	}

}

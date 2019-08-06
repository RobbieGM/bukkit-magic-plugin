package com.gmail.robbiem.BukkitPluginMain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CooldownBossBar implements Listener {
	
	PlayerCooldownManager cooldownManager;
	Player player;
	BossBar bossBar;
	Map<String, UseableItem> useableItemSlots = new HashMap<>();
	
	public CooldownBossBar(PlayerCooldownManager cooldownManager, Player player) {
		this.cooldownManager = cooldownManager;
		this.player = player;
		this.bossBar = player.getServer().createBossBar("Cooldown", BarColor.WHITE, BarStyle.SOLID);
		this.bossBar.addPlayer(player);

		cooldownManager.plugin.getServer().getPluginManager().registerEvents(this, cooldownManager.plugin);
	}
	
	public String getItemNameBySlot(int slot) {
		ItemStack item = player.getInventory().getItem(slot);
		if (item != null && item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		} else return null;
	}
	
	public void useItem(UseableItem item, int slot) {
		String itemName = getItemNameBySlot(slot);
		if (itemName == null) {
			throw new IllegalArgumentException("Item in slot did not have a display name but was used.");
		}
		useableItemSlots.put(itemName, item);
		bossBar.setProgress(1);
		cooldownManager.setPlayerCooldown(player, item.getPlayerCooldown());
		update();
	}
	
	@EventHandler
	public void onPlayerSwitchItem(PlayerItemHeldEvent e) {
		if (e.getPlayer().getUniqueId().equals(player.getUniqueId())) {
			update(e.getNewSlot());
		}
	}
	
	public void update() {
		update(player.getInventory().getHeldItemSlot());
	}
	
	public void update(int inventorySlot) {
		UseableItem itemInHand = useableItemSlots.get(getItemNameBySlot(inventorySlot));
//		long maxCooldown = cooldownManager.getCooldown(player).endDatetime;
//		if (itemInHand != null && itemInHand.getItemCooldown() > maxCooldown) {
//			maxCooldown = itemInHand.getItemCooldown();
//		}
//		long cooldownEndDatetime = cooldownManager.getCooldownDatetime(player, itemInHand);
//		long cooldownStartDatetime = cooldownEndDatetime - maxCooldown;
		PlayerCooldownManager.Cooldown cooldown = cooldownManager.getCooldown(player, itemInHand);
		long now = new Date().getTime();
		long timeWaited = now - cooldown.startDatetime;
		double fractionOfCooldownDone = (double) timeWaited / cooldown.duration();
		if (fractionOfCooldownDone < 1) {
			if (!bossBar.isVisible())
				bossBar.setVisible(true);
			bossBar.setProgress(1 - fractionOfCooldownDone);
			player.getServer().getScheduler().scheduleSyncDelayedTask(cooldownManager.plugin, this::update, 1);
		} else {
			bossBar.setProgress(0);
			bossBar.setVisible(false);
		}
	}
}

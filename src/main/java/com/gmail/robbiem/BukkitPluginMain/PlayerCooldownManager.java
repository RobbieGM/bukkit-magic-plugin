package com.gmail.robbiem.BukkitPluginMain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.robbiem.BukkitPluginMain.wands.LeftClickableWand;

public class PlayerCooldownManager {
	
	public static class Cooldown {
		long startDatetime;
		long endDatetime;
		
		public Cooldown(long start, long end) {
			this.startDatetime = start;
			this.endDatetime = end;
		}
		
		public Cooldown(long time) {
			long now = new Date().getTime();
			startDatetime = now;
			endDatetime = now + time;
		}
		
		public long duration() {
			return endDatetime - startDatetime;
		}
		
		public static Cooldown none() {
			return new Cooldown(0, 0);
		}
		
		public static Cooldown longest(Cooldown... cooldowns) {
			return Arrays.asList(cooldowns).stream().max(Comparator.comparing(c -> c.endDatetime)).get();
		}
		
	}
	
	Map<ItemCooldownKey, Cooldown> itemSpecificCooldowns;
	Map<UUID, Cooldown> playerCooldowns;
	Map<UUID, CooldownBossBar> bossBars;
	public JavaPlugin plugin;
	
	public PlayerCooldownManager(JavaPlugin plugin) {
		itemSpecificCooldowns = new HashMap<>();
		playerCooldowns = new HashMap<>();
		bossBars = new HashMap<>();
		this.plugin = plugin;
	}
	
	CooldownBossBar getBossBar(Player player) {
		CooldownBossBar bb;
		if (!bossBars.containsKey(player.getUniqueId())) {
			bb = new CooldownBossBar(this, player);
			bossBars.put(player.getUniqueId(), bb);
		} else {
			bb = bossBars.get(player.getUniqueId());
		}
		return bb;
	}
	
	public void useItemAlt(Player player, LeftClickableWand item) {
		setPlayerCooldown(player, item.getAltPlayerCooldown());
		itemSpecificCooldowns.put(new ItemCooldownKey(player, item), new Cooldown(item.getAltItemCooldown()));
		getBossBar(player).useItem(item);
	}
	
	public void useItem(Player player, UseableItem item) {
		setPlayerCooldown(player, item.getPlayerCooldown());
		itemSpecificCooldowns.put(new ItemCooldownKey(player, item), new Cooldown(item.getItemCooldown()));
		getBossBar(player).useItem(item);
	}
	
	public void setPlayerCooldown(Player player, long cooldown) {
		setPlayerCooldown(player, cooldown, false);
	}
	
	public void setPlayerCooldown(Player player, long cooldown, boolean force) {
		Cooldown newCooldown = new Cooldown(cooldown);
		if (newCooldown.endDatetime > getCooldown(player).endDatetime || force) {
			playerCooldowns.put(player.getUniqueId(), new Cooldown(cooldown));
		}
	}
	
	/**
	 * Gets a player's {@link Cooldown}.
	 * @param player The player to query the cooldown of
	 * @return The Cooldown object
	 */
	public Cooldown getCooldown(Player player) {
		UUID uuid = player.getUniqueId();
		return playerCooldowns.containsKey(uuid) ? playerCooldowns.get(uuid) : Cooldown.none();
	}
	
	/**
	 * Gets a player's cooldown for an item, taking into account both player and item cooldowns.
	 * @param player The player to query the cooldown of
	 * @param item The item to query the item-specific cooldown of
	 * @return The {@link Cooldown} with the greater {@link Cooldown#endDatetime}
	 */
	public Cooldown getCooldown(Player player, UseableItem item) {
		ItemCooldownKey key = item != null && player != null ? new ItemCooldownKey(player, item) : null;
		Cooldown itemCooldown = key != null && itemSpecificCooldowns.containsKey(key) ? itemSpecificCooldowns.get(key) : Cooldown.none();
		return Cooldown.longest(itemCooldown, getCooldown(player));
	}
	
	public boolean playerMayUseItem(Player player, UseableItem item) {
		long now = new Date().getTime();
		return now > getCooldown(player, item).endDatetime;
	}
}
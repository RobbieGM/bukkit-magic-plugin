package com.gmail.robbiem.BukkitPluginMain.runes;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import com.gmail.robbiem.BukkitPluginMain.Main;

public class RuneOfInfestation extends Rune {
	
	public static final int DISTANCE = 10;

	public RuneOfInfestation(Main plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled() || e.getPlayer() == null) return; // BlockProtector/shulkers = no silverfish spawned
		Player breaker = e.getPlayer();
		World world = e.getPlayer().getWorld();
		for (Player player: world.getPlayers()) {
			if (playerHasRune(player) && e.getBlock().getLocation().distance(player.getLocation()) <= DISTANCE && !player.equals(breaker)) {
				consumeRune(player);
				playRuneEffect(player);
				for (int i = 0; i < 3; i++) {
					Silverfish silverfish = (Silverfish) world.spawnEntity(e.getBlock().getLocation(), EntityType.SILVERFISH);
					silverfish.setTarget(breaker);
				}
			}
		}
	}

	@Override
	String getRuneName() {
		return "Rune of Infestation";
	}

}

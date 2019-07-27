package com.gmail.robbiem.BukkitPluginMain;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RestartGameCommand implements CommandExecutor {
	
	JavaPlugin plugin;
	
	public RestartGameCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Server server = sender.getServer();
		World hungerGames = server.getWorld(Main.HUNGER_GAMES_WORLD);
		World lobby = server.getWorld(Main.HUNGER_GAMES_WAITING_ROOM);
		
		if (hungerGames != null) {
			for (Player player: hungerGames.getPlayers()) {
				player.teleport(new Location(lobby, 8, 4, 8));
				player.setGameMode(GameMode.ADVENTURE);
			}
		}
		hungerGames = reload(sender.getServer(), "HungerGames");
		final World hg = hungerGames;
		server.broadcastMessage("Teleporting everyone to the hunger games world in 5 seconds.");
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			for (Player player: lobby.getPlayers()) {
				player.teleport(new Location(hg, 933, 74, 1506));
			}
			server.broadcastMessage("Starting the game in 10 seconds.");
		}, 20 * 5);
		server.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			server.dispatchCommand(sender, "go");
		}, 20 * 5 + 20 * 10);
		
		return true;
	}
	
	World reload(Server server, String mapName) {
		if (!server.unloadWorld(mapName, false)) {
			server.broadcastMessage("This world is being loaded for the first time.");
		} else {
			server.broadcastMessage("The world is being reloaded.");
		}
		return server.createWorld(new WorldCreator(mapName));
	}
	
	// tp to lobby (unnecessary first time but ok), load main world, then /go
}

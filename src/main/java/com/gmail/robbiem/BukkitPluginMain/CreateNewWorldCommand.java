package com.gmail.robbiem.BukkitPluginMain;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateNewWorldCommand implements CommandExecutor {
	
	Main plugin;
	
	public CreateNewWorldCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Server server = sender.getServer();
		sender.getServer().broadcastMessage("Loading the new world...");
		String worldName = "FreshWorld" + new Random().nextInt(Integer.MAX_VALUE);
		plugin.primaryWorldName = worldName;
		World newWorld = server.createWorld(new WorldCreator(worldName));
		plugin.primaryWorld = newWorld;

		sender.getServer().broadcastMessage("Done. Teleporting all players to the new world.");

		for (World w: server.getWorlds()) {
			for (Player player: w.getPlayers()) {
				player.teleport(newWorld.getSpawnLocation());
				player.setGameMode(GameMode.ADVENTURE);
			}
		}
		
		return true;
	}
}

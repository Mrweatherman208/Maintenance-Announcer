package hjw.maintenanceannouncer;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	    	
	private boolean started = false; // Var for if the maintenance to the server is active.
	
	// Plugin has been enabled.
	@Override
	public void onEnable() {
   	 getLogger().info("Maintenance Announcer is enabled.");
		createConfig();
		// Check if the "Maintenance" string in the config says "true".
   	 if (getConfig().getString("Maintenance") == "true") {
			startEvent();
   	 }
	}
	
	// Plugin has been disabled.
	@Override
	public void onDisable() { 
		getLogger().info("Maintenance Announcer is disabled."); // Log that Maintenance Announcer has been disabled.
	}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
		// Statements for the maintenance commands.
		if (cmd.getName().equalsIgnoreCase("maintenance")) {
			reloadConfig();
			// Switch for the arugment length.
			switch (args.length) {
				case 0:
				if (getConfig().getString("adminOnlyMaintenance") == "true") {
					if(sender.hasPermission("hjw.maintenanceannouncer.adminsee")) {
		    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance ongoing message")));
					}
		   	 }
				if(sender.hasPermission("hjw.maintenanceannouncer.see")) {
				   	 if (getConfig().getString("Maintenance") == "true") {
	 			    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance ongoing message")));
				   	 } else {
	 			    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance not ongoing message")));
				   	 }
				}
				return true;
				case 1: // User just used one arguement in their command.
				// Check for "start" or "begin" being the first argument.
				if (args[0].equalsIgnoreCase("start") || (args[0].equalsIgnoreCase("begin"))) {
					if(sender.hasPermission("hjw.maintenanceannouncer.start")) {
						getConfig().set("Maintenance", true);
						saveConfig();
						reloadConfig();
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance has started")));
						startEvent();
					}
					return true;
				}
				// Check for "stop" or "end" being the first argument.
				if (args[0].equalsIgnoreCase("stop") || (args[0].equalsIgnoreCase("end"))) {
					if(sender.hasPermission("hjw.maintenanceannouncer.end")) {
						getConfig().set("Maintenance", false);
						saveConfig();
						reloadConfig();
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance has ended")));
						HandlerList.unregisterAll(this);
					}
					return true;
				}
				// Check for "admin" being the first argument.
					if (args[0].equalsIgnoreCase("admin")) {
						if(sender.hasPermission("hjw.maintenanceannouncer.adminsee")) {
							if(sender.hasPermission("hjw.maintenanceannouncer.adminsee")) {
							if (getConfig().getString("adminOnlyMaintenance") == "true") {
					    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance ongoing message")));
								} else {
						    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance not ongoing message")));
								}
					   	 }
						}
						return true;
				}
				// Reload Plugin
				if (args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("hjw.maintenanceannouncer.reload")) {
							if (getConfig().getString("Allow reloads to Maintenance Announcer") == "true") {
							reloadConfig();
							sender.sendMessage(ChatColor.GREEN + "Maintenance Announcer successfully reloaded!");
							} else {
							sender.sendMessage(ChatColor.RED + "Reloading of Maintenance Announcer was disabled in the config file!");
							}
					}
					return true;
					}
						// Invalid Argument
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Invalid argument message")));
				case 2: // User used 2 arguements in their command.
				// Check for "admin" being the first argument.
				if (args[0].equalsIgnoreCase("admin")) {
					if (args[1].equalsIgnoreCase("start") || (args[1].equalsIgnoreCase("begin"))) {
						if(sender.hasPermission("hjw.maintenanceannouncer.adminstart")) {
							adminBroadcastStart();
							getConfig().set("adminOnlyMaintenance", true);
							saveConfig();
							reloadConfig();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Sent to all admins message")));
						}
						return true;
					}
					// Check for "stop" or "end" being the second argument.
					if (args[1].equalsIgnoreCase("stop") || (args[1].equalsIgnoreCase("end"))) {
						if(sender.hasPermission("hjw.maintenanceannouncer.adminend")) {
							adminBroadcastStop();
							getConfig().set("adminOnlyMaintenance", false);
							saveConfig();
							reloadConfig();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Sent to all admins message")));
							}
						return true;
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Invalid argument message")));
					}
				return true;
					default: sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Two arguments are needed message")));
				
			}
	}
		return false;
}
	
	// Maintenance was started.
	private void startEvent() {
		// Check if maintenance has already been started.
		if (started == false) {
		PluginManager pm = getServer().getPluginManager();
		Listener listener = new Listener(this);
		pm.registerEvents(listener, this);
		started = true;
		}
	}
	
	private void adminBroadcastStart() {
    	for (Player player : Bukkit.getServer().getOnlinePlayers()) {
    		if (player.isOp()) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance has started")));
    		}
    	}
	}

	private void adminBroadcastStop() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.isOp()) {
player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance has ended")));
			}
		}
	}

	private void createConfig() {
	    try {
			// Check if the "MaintenanceAnnouncer" directory exists.
	        if (getDataFolder().exists() == false) { 
				getDataFolder().mkdirs(); // Make directory using the built-in bukkit function.
			}

	        File file = new File(getDataFolder(), "config.yml"); // Introduce the new file, spcify the directory and file name.

			// Check if the config file actually exists.
	        if (file.exists() == false) {
			   	getLogger().info("Config file not found! Creating one!"); // Log that a config file is being made.
	            saveDefaultConfig(); // Use the save default bukkit function.
	        } else {
	        	getLogger().info("Config file found! Loaded!");
	        }
	    } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
}

// You are of God, little children, and have overcome them, because He who is
// in you is greater than he who is in the world. - I John 4:4

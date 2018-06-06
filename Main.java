package gamercraftbros.maintenanceannouncer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	    	
	private boolean started = false;
	
	@Override
	public void onEnable() {
   	 getLogger().info("Maintenance Announcer is enabled.");
   	 createConfig();
   	 if (getConfig().getString("Automatically check for updates") == "true") {
   	   	 try {
			checkUpdate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	 }
   	 if (getConfig().getString("Maintenance") == "true") {
			startEvent();
   	 }
	}
	
	@Override
	public void onDisable() { 
		getLogger().info("Maintenance Announcer is disabled.");
		}
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
		// Statements for the maintenance commands.
		if (cmd.getName().equalsIgnoreCase("maintenance")) {
			reloadConfig();
			if (args.length == 0) {
				if (getConfig().getString("PrivateMaintenance") == "true") {
					if(sender.hasPermission("gamercraftbros.maintenanceannouncer.adminsee")) {
		    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance ongoing message")));
					}
		   	 }
				if(sender.hasPermission("gamercraftbros.maintenanceannouncer.see")) {
				   	 if (getConfig().getString("Maintenance") == "true") {
	 			    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance ongoing message")));
				   	 } else {
	 			    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance not ongoing message")));
				   	 }
					return false;
				}
			}
		if (args.length == 2) {
		if (args[0].equalsIgnoreCase("admin")) {
			if (args[1].equalsIgnoreCase("start") || (args[1].equalsIgnoreCase("begin"))) {
				if(sender.hasPermission("gamercraftbros.maintenanceannouncer.adminstart")) {
					adminBroadcastStart();
					getConfig().set("PrivateMaintenance", true);
					saveConfig();
					reloadConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Sent to all admins message")));
				}
			}
			if (args[1].equalsIgnoreCase("stop") || (args[1].equalsIgnoreCase("end"))) {
				if(sender.hasPermission("gamercraftbros.maintenanceannouncer.adminend")) {
					adminBroadcastStop();
					getConfig().set("PrivateMaintenance", false);
					saveConfig();
					reloadConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Sent to all admins message")));
					}
			}
			return false;
			}
		}
		if (args.length == 1) {
		if (args[0].equalsIgnoreCase("start") || (args[0].equalsIgnoreCase("begin"))) {
			if(sender.hasPermission("gamercraftbros.maintenanceannouncer.start")) {
				getConfig().set("Maintenance", true);
				saveConfig();
				reloadConfig();
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance has started")));
				startEvent();
			}
		}
		if (args[0].equalsIgnoreCase("stop") || (args[0].equalsIgnoreCase("end"))) {
			if(sender.hasPermission("gamercraftbros.maintenanceannouncer.end")) {
				getConfig().set("Maintenance", false);
				saveConfig();
				reloadConfig();
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix") + getConfig().getString("Maintenance has ended")));
				HandlerList.unregisterAll(this);
			}
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("gamercraftbros.maintenanceannouncer.reload")) {
			   	 if (getConfig().getString("Allow reloads to Maintenance Announcer") == "true") {
					reloadConfig();
					sender.sendMessage(ChatColor.GREEN + "Maintenance Announcer successfully reloaded!");
			   	 } else {
					sender.sendMessage(ChatColor.RED + "Reloading of Maintenance Announcer was disabled in the config file!");
			   	 }
			}
		    }
		} else {
			sender.sendMessage("2 arguments are needed!");
		}
	}
		return false;
}
	
	private void verifyConfig() {
		if ((getConfig().getInt("configversion") == 2) || (getConfig().getInt("configversion") == 3) || (getConfig().getInt("config version") == 3.6)) {
	   	   	 getLogger().warning("Maintenance Announcer config file is from version 3.");
	   	   	 getLogger().warning("This will cause problems. Resetting config file...");
		        File file = new File(getDataFolder(), "config.yml");
		        file.delete();
	            saveDefaultConfig();
		   	   	getLogger().info("Config file reset.");
	}
	}
	
	private void startEvent() {
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
    				}}}

	private void adminBroadcastStop() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.isOp()) {
player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Admin only prefix") + getConfig().getString("Maintenance has ended")));
			}}}

	private void createConfig() {
	    try {
	        if (!getDataFolder().exists()) {getDataFolder().mkdirs();}
	        File file = new File(getDataFolder(), "config.yml");
	        if (!file.exists()) {
			   	getLogger().info("Config file not found! Creating one!");
	            saveDefaultConfig();
	        } else {
	        	getLogger().info("Config file found! Loaded!");
	        	verifyConfig();
	        	}
	    } catch (Exception e) {e.printStackTrace(); }}
	
	private void checkUpdate() throws IOException {
		final Updater updater = new Updater(this, 24875, false);
		final Updater.UpdateResult result = updater.getResult();
	    switch (result) {
	     case FAIL_SPIGOT: {
	         break;
	     }
	     case NO_UPDATE: {
	         break;
	     }
	     case UPDATE_AVAILABLE: {
	    	 String oldVer = (getDescription().getVersion());
	    	 String newVer = (updater.getVersion());
	    	 int compare = oldVer.compareToIgnoreCase(newVer);
	    	 if(compare < 0) {
	    		 Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--------------------SOFTWARE UPDATE--------------------");
		    	 Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Maintenance Announcer " + updater.getVersion() + " is now available.");
		    	 Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "What's new:");
		 		  URL oracle = new URL("http://68.185.143.95/maintenanceAnnouncerLatestChanges.txt");
		 		    BufferedReader in = new BufferedReader(
		 		    new InputStreamReader(oracle.openStream()));

		 		    String inputLine;
		 		    while ((inputLine = in.readLine()) != null)
			    	 Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + inputLine);
		 		    in.close();
			 Bukkit.getConsoleSender().sendMessage("");
    		     Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Get the update at: www.spigotmc.org/resources/24875/ ");
	    		 Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--------------------------------------------------------");
	    	 }
	     }
	     default: {
	         break;
	     }}}
}

// You are of God, little children, and have overcome them, because He who is
// in you is greater than he who is in the world. - I John 4:4

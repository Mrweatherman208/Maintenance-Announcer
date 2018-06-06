package hjw.maintenanceannouncer;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener implements org.bukkit.event.Listener {
	
	private Main plugin;
	
	public Listener (Main plugin) { this.plugin = plugin; }
	
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) { 
		event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("Maintenance ongoing message"))); 
		}
	}

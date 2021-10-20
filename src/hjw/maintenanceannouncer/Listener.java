package hjw.maintenanceannouncer;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener implements org.bukkit.event.Listener {

	private Main plugin;

	public Listener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		
		// This feature automatically notifies players when they join on whether or not maintenance is ongoing to the server at the current moment.
		if(event.getPlayer().hasPermission("hjw.maintenanceannouncer.see") && plugin.started == true) {
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Prefix") + plugin.getConfig().getString("Maintenance ongoing message")));
		}
		
		// Check if user can see if admin only maintenance has started.
		if(event.getPlayer().hasPermission("hjw.maintenanceannouncer.adminsee") && plugin.adminOnlyStarted == true) {
			event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Admin only prefix") + plugin.getConfig().getString("Maintenance ongoing message")));
		}
	}
}
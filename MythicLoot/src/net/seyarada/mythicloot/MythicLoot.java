package net.seyarada.mythicloot;

import org.bukkit.plugin.java.JavaPlugin;

public class MythicLoot extends JavaPlugin {
	
	net.seyarada.mythicloot.events.DamagedEvent DamagedEvent = new net.seyarada.mythicloot.events.DamagedEvent();
	net.seyarada.mythicloot.events.DeathEvent   DeathEvent 	 = new net.seyarada.mythicloot.events.DeathEvent();
	net.seyarada.mythicloot.events.ItemPickup   Pickup 	     = new net.seyarada.mythicloot.events.ItemPickup();
	net.seyarada.mythicloot.events.QuitEvent    QuitEvent 	 = new net.seyarada.mythicloot.events.QuitEvent();
	net.seyarada.mythicloot.events.Trackers     Trackers 	 = new net.seyarada.mythicloot.events.Trackers();
	 
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(DamagedEvent, this);
		this.getServer().getPluginManager().registerEvents(DeathEvent, this);
		this.getServer().getPluginManager().registerEvents(QuitEvent, this);
		this.getServer().getPluginManager().registerEvents(Trackers, this);
		this.getServer().getPluginManager().registerEvents(Pickup, this);
		new Config();
	}
		
	@Override
	public void onDisable() {

	}
	
}

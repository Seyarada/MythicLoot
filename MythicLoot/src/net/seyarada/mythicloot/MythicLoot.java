package net.seyarada.mythicloot;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MythicLoot extends JavaPlugin {
	
	net.seyarada.mythicloot.events.DamagedEvent DamagedEvent = new net.seyarada.mythicloot.events.DamagedEvent();
	net.seyarada.mythicloot.events.DeathEvent   DeathEvent 	 = new net.seyarada.mythicloot.events.DeathEvent();
	net.seyarada.mythicloot.events.ItemPickup   Pickup 	     = new net.seyarada.mythicloot.events.ItemPickup();
	net.seyarada.mythicloot.events.QuitEvent    QuitEvent 	 = new net.seyarada.mythicloot.events.QuitEvent();
	net.seyarada.mythicloot.events.Trackers     Trackers 	 = new net.seyarada.mythicloot.events.Trackers();

	private static Economy econ = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(DamagedEvent, this);
		this.getServer().getPluginManager().registerEvents(DeathEvent, this);
		this.getServer().getPluginManager().registerEvents(QuitEvent, this);
		this.getServer().getPluginManager().registerEvents(Trackers, this);
		this.getServer().getPluginManager().registerEvents(Pickup, this);
		new Config().generate();

		setupEconomy();

	}
		
	@Override
	public void onDisable() {

	}

	private void setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
	}

	public static Economy getEconomy() {
		return econ;
	}
	
}

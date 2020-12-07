package net.seyarada.mythicloot.events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.seyarada.mythicloot.Data;

public class Trackers implements Listener {

	@EventHandler
	public void onSpawn(MythicMobSpawnEvent e) {
		boolean shouldStore = e.getMobType().getConfig().getList("Rewards")!=null&&e.getMobType().getConfig().getList("Rewards").size()>0;
		if(shouldStore) {
			new Data(e.getEntity().getUniqueId()).register();
		}
	}
	
	@EventHandler
	public void onDespawn(MythicMobDespawnEvent e) {
		UUID uuid = e.getEntity().getUniqueId();
		Data data = new Data(uuid);
		if(data.isRegistered()) {
			data.forget();
			data.remove();
		};
	}
	
}

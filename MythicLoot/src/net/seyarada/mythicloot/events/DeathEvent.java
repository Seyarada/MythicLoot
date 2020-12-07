package net.seyarada.mythicloot.events;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.seyarada.mythicloot.Data;
import net.seyarada.mythicloot.DropItem;
import net.seyarada.mythicloot.rank.Board;

public class DeathEvent implements Listener {
	
	@EventHandler
	public void onDeath(MythicMobDeathEvent e) {
		UUID uuid = e.getEntity().getUniqueId();
		Data data = new Data(uuid);
		if(data.isRegistered() && data.exists() && data.get().size()>0) {
			
			// General data that doesn't needs to be collected in the foreach
			double HP = data.get().values().stream().mapToDouble(Double::valueOf).sum();
		
			
			if (e.getMobType().getConfig().getBoolean("Options.AnnounceRank")) {
				
				new Board(new LinkedList<>(data.get().entrySet()), uuid, HP);
				
			}
			
			
			for(Object i:e.getMobType().getConfig().getList("Rewards")) {
				
				MythicLineConfig mlc = new MythicLineConfig((String) i);
				new DropItem().prepareDrop(mlc, data, HP, e.getEntity(), new LinkedList<>(data.get().entrySet()));	
				
			}
			data.forget();
			data.remove();
		}
	}
	
}

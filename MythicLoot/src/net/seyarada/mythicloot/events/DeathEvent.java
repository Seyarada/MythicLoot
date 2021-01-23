package net.seyarada.mythicloot.events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import io.lumine.xikage.mythicmobs.io.MythicConfig;
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

			MythicConfig config = e.getMobType().getConfig();
			boolean rank = config.getBoolean("Options.AnnounceRank");
			boolean score = config.getBoolean("Options.AnnounceScore");
			boolean relative = config.getBoolean("Options.Relative");
			if (rank||score) {
				new Board(new LinkedList<>(data.get().entrySet()), uuid, HP, rank, score, relative);
			}
			
			
			for(Object i:e.getMobType().getConfig().getList("Rewards")) {
				
				MythicLineConfig mlc = new MythicLineConfig((String) i);
				new DropItem().prepareDrop(mlc, data, HP, e.getEntity(), new LinkedList<>(data.get().entrySet()));
				
			}
			new Trackers().nuke();
			data.forget();
			data.remove();
		}
	}
	
}

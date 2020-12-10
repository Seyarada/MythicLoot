package net.seyarada.mythicloot.events;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import net.seyarada.mythicloot.Data;

public class QuitEvent implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		String name = e.getEntity().getName();
		remove(name);
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		String name = e.getPlayer().getName();
		remove(name);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String name = e.getPlayer().getName();
		remove(name);
		}
	
	public void remove(String name) {
		Data data = new Data(null);
		Iterator<UUID> i = data.getIterator();
		while (i.hasNext()) {
			UUID mUUID = i.next();
			data = new Data(mUUID);
			
			AbstractEntity entity = BukkitAdapter.adapt(Bukkit.getEntity(mUUID));
			if(entity==null) return;
			
			MythicConfig config = MythicMobs.inst().getMobManager().getMythicMobInstance(entity).getType().getConfig();
			if (data.exists()) {
				Set<String> k = data.keys();
				if (k != null && k.contains(name)) {
					if(config.getBoolean("Options.ResetHeal")&&data.get().size()>0) {
						LivingEntity a = ((LivingEntity) Bukkit.getEntity(mUUID));
						if(a!=null) {
							double health = a.getHealth()+data.get().get(name);
							double maxHP = a.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
							a.setHealth(Math.min(health, maxHP));
						}
					}
					k.remove(name);
				}
			}
		}
	}
	
}

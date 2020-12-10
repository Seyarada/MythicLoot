package net.seyarada.mythicloot.events;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.seyarada.mythicloot.Data;

public class DamagedEvent implements Listener {

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		
		Entity entity = event.getEntity();
		Data data = new Data(entity.getUniqueId());
		
		if (data.isRegistered() && event.getDamager() instanceof Player) {
			
			double finalDamage = event.getFinalDamage();
			double health = ((LivingEntity)entity).getHealth();
			if(finalDamage>health) finalDamage = health;
			
			if (data.exists())
			{
					// Gets the damage map of the entity
					HashMap<String, Double> damageMap = data.get();
					
					// Checks if the entity already has that player mapped
					if (damageMap.containsKey(event.getDamager().getName())) {
						// Updates the map to add the new damage
						damageMap.put(event.getDamager().getName(), damageMap.get(event.getDamager().getName()) + finalDamage);
					} else {
						// Creates a new entry in the hashmap with the player and damage
						damageMap.put(event.getDamager().getName(), finalDamage);}
					// Pushes the data to the main hashmap
					data.put(damageMap);

			} else {
				// Stores who damaged the mob and the amount
				HashMap<String, Double> damage = new HashMap<>();
				damage.put( event.getDamager().getName(), finalDamage );
				// Creates a new entry in the hashmap with the player and damage
				data.put(damage);
				}
				
			
		}
	}
	
}
